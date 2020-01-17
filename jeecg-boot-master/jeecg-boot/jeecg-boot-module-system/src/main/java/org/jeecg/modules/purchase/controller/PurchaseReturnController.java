package org.jeecg.modules.purchase.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.entity.InventoryOutMtl;
import org.jeecg.modules.inventory.service.InventoryOutMtlService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.purchase.dto.PurchaseReturnInDto;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.PurchaseDtl;
import org.jeecg.modules.purchase.entity.PurchaseReturn;
import org.jeecg.modules.purchase.entity.PurchaseReturnDtl;
import org.jeecg.modules.purchase.service.IPurchaseDtlService;
import org.jeecg.modules.purchase.service.IPurchaseReturnDtlService;
import org.jeecg.modules.purchase.service.IPurchaseReturnService;
import org.jeecg.modules.purchase.service.IPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(tags = "采购退货")
@RestController
@RequestMapping("/purchasereturn")
public class PurchaseReturnController extends JeecgController<PurchaseReturn, IPurchaseReturnService> {

    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Autowired
    private IPurchaseService purchaseService;
    @Autowired
    private IPurchaseDtlService purchaseDtlService;

    @Autowired
    private IPurchaseReturnService purchasereturnService;
    @Autowired
    private IPurchaseReturnDtlService purchaseReturnDtlService;

    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private InventoryOutMtlService inventoryOutMtlService;

    @GetMapping("/getPaged")
    public Result<?> queryPageList(PurchaseReturn purchasereturn,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req)
    {
        QueryWrapper<PurchaseReturn> queryWrapper = QueryGenerator.initQueryWrapper(purchasereturn, req.getParameterMap());
        Page<PurchaseReturn> page = new Page<>(pageNo, pageSize);
        IPage<PurchaseReturn> pageList = purchasereturnService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody PurchaseReturnInDto dto){
        Purchase purchase = purchaseService.getById(dto.getSourceId());
        if (purchase!=null){
            //退货表
            PurchaseReturn rtn = new PurchaseReturn();
            String code = billCodeBuilderService.getBillCode(BillType.PURCHASERETURNORDER.getId());
            rtn.setCode(code);
            rtn.setWarehouseId(purchase.getWarehouseId());
            rtn.setAmount(purchase.getTotalamount());
            rtn.setSourceId(purchase.getId());
            rtn.setBilldate(DateUtils.getDate());
            purchasereturnService.save(rtn);

            //出库单
            String stockcode = billCodeBuilderService.getBillCode(BillType.STOCKING.getId());
            InventoryOut inventoryOut = new InventoryOut();
            inventoryOut.setSourceId(purchase.getId());
            inventoryOut.setCode(stockcode);
            inventoryOut.setBillType(0);
            inventoryOut.setSourceBillType(0);
            inventoryOut.setWarehouseId(purchase.getWarehouseId());
            inventoryOut.setPutOutTime(dto.getPutOutTime());
            inventoryOut.setBillStatus(0);
            inventoryOutService.save(inventoryOut);

            List<PurchaseDtl> list = purchaseDtlService.queryBySourceId(purchase.getId());
            if (list.size() > 0){
                for (PurchaseDtl item:list){
                    // 退货商品表
                    PurchaseReturnDtl purchaseReturnDtl = new PurchaseReturnDtl();
                    purchaseReturnDtl.setSourceId(rtn.getId());
                    purchaseReturnDtl.setCode(code);
                    purchaseReturnDtl.setMtlId(item.getMtlId());
                    purchaseReturnDtl.setUnitId(item.getUnitId());
                    purchaseReturnDtl.setQuantity(item.getQuantity());
                    purchaseReturnDtl.setPrice(item.getPrice());
                    purchaseReturnDtl.setDiscount(item.getDiscount());
                    purchaseReturnDtl.setAmount(item.getAmount());
                    purchaseReturnDtlService.save(purchaseReturnDtl);

                    //退货单商品详情
                    InventoryOutMtl inventoryOutMtl = new InventoryOutMtl();
                    inventoryOutMtl.setCode(stockcode);
                    inventoryOutMtl.setSourceId(inventoryOut.getId());
                    inventoryOutMtl.setMtlId(item.getMtlId());
                    inventoryOutMtl.setUnitId(item.getUnitId());
                    inventoryOutMtl.setQuantity(item.getQuantity());
                    inventoryOutMtlService.save(inventoryOutMtl);
                }
            }
        }
        return Result.ok("退货成功");
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody PurchaseReturnInDto dto){
        InventoryOut inventoryOut = inventoryOutService.getById(dto.getId());
        if (inventoryOut != null){
            inventoryOut.setPutOutTime(dto.getPutOutTime());
            inventoryOutService.updateById(inventoryOut);
            return Result.ok("编辑成功");
        }
        return Result.ok("编辑失败");
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id){
        purchasereturnService.removeById(id);
//        purchaseService.removeById(id);
//        purchasedtlService.removeBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        purchasereturnService.removeByIds(Arrays.asList(ids.split(",")));
//        purchaseService.removeByIds(Arrays.asList(ids.split(",")));
//        purchasedtlService.removeBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id){
        PurchaseReturn purchasereturn = purchasereturnService.getById(id);
//        Purchase purchase = purchaseService.getById(id);
//        System.out.println(purchase.getId());
        if (purchasereturn == null){
            return Result.ok("未找到对应数据");
        }
//        Purchasedtldto purchasedtldto = new Purchasedtldto();
//        purchasedtldto.setId(purchase.getId());
//        purchasedtldto.setVendorId(purchase.getVendorId());
//        purchasedtldto.setContent(purchase.getContent());
//        purchasedtldto.setWarehouseId(purchase.getWarehouseId());
//        purchasedtldto.setAccount(purchase.getAccount());
//        purchasedtldto.setPayamount(purchase.getPayamount());
//        purchasedtldto.setBilldate(purchase.getBilldate());
//        purchasedtldto.setTotalamount(purchase.getTotalamount());
//        purchasedtldto.setCreateTime(purchase.getCreateTime());
//        purchasedtldto.setDetaillist(purchasedtlService.queryBySourceId(purchase.getId()));
        return Result.ok(purchasereturn);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurchaseReturn purchasereturn){
        return super.exportXls(request, purchasereturn, PurchaseReturn.class, "采购列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response){
        return super.importExcel(request, response, PurchaseReturn.class);
    }













}
