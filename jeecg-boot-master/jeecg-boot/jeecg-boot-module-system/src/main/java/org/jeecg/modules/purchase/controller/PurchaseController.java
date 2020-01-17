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
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryInMtlService;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.purchase.dto.PurchaseInventoryDto;
import org.jeecg.modules.purchase.dto.PurchaseDtlDto;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.PurchaseDtl;
import org.jeecg.modules.purchase.service.IPurchaseService;
import org.jeecg.modules.purchase.service.IPurchaseDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "采购列表")
@RestController
@RequestMapping("/purchase")
public class PurchaseController extends JeecgController<Purchase, IPurchaseService> {
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryInMtlService inventoryInMtlService;
    @Autowired
    private InventoryOutService inventoryOutService;

    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private IPurchaseDtlService purchasedtlService;

    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @GetMapping("/getPaged")
    public Result<?> queryPageList(Purchase purchase,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req)
    {
        QueryWrapper<Purchase> queryWrapper = QueryGenerator.initQueryWrapper(purchase, req.getParameterMap());
        Page<Purchase> page = new Page<>(pageNo, pageSize);
        IPage<Purchase> pageList = purchaseService.page(page, queryWrapper);
        for (Purchase item :pageList.getRecords()){
            item.setInventoryin(inventoryInService.getOne(new LambdaQueryWrapper<InventoryIn>().eq(InventoryIn::getSourceId, item.getId())));
            item.setInventoryOut(inventoryOutService.getOne(new LambdaQueryWrapper<InventoryOut>().eq(InventoryOut::getSourceId, item.getId())));
        }
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody PurchaseDtlDto purchasedtldto){
        PurchaseInventoryDto dto = new PurchaseInventoryDto();
        dto.setMsg("添加失败");

        // 采购主表
        String code = billCodeBuilderService.getBillCode(BillType.PURCHASEORDER.getId());
        purchasedtldto.setCode(code);
        purchaseService.save(purchasedtldto);

        // 入库单主表
        InventoryIn model = new InventoryIn();
        model.setBillStatus(0);
        model.setSourceId(purchasedtldto.getId());
        model.setBillType(BillType.STOCKING.getId());
        model.setCode(billCodeBuilderService.getBillCode(BillType.STOCKING.getId()));
        inventoryInService.save(model);

        List<InventoryInMtl> detaillist = new ArrayList<>();
        if (purchasedtldto.getDetaillist().size() > 0){
            for (PurchaseDtl item: purchasedtldto.getDetaillist()){
                //采购商品详情
                item.setCode(code);
                item.setSourceId(purchasedtldto.getId());
                purchasedtlService.save(item);

                //入库单商品详情
                InventoryInMtl inventoryInMtl = new InventoryInMtl();
                inventoryInMtl.setCode(billCodeBuilderService.getBillCode(BillType.STOCKING.getId()));
                inventoryInMtl.setSourceId(model.getId());
                inventoryInMtl.setMtlId(item.getMtlId());
                inventoryInMtl.setQuantity(new BigDecimal(item.getQuantity()));
                inventoryInMtl.setUnitId(item.getUnitId());
                inventoryInMtlService.save(inventoryInMtl);
                detaillist.add(inventoryInMtl);
            }
        }
        model.setInventoryInMtls(detaillist);

        dto.setMsg("添加成功");
        dto.setInventory(model);

        return Result.ok(dto);
    }

    @PutMapping("/edit")
    @Transactional
    public Result<?> edit(@RequestBody PurchaseDtlDto purchasedtldto){
        PurchaseInventoryDto dto = new PurchaseInventoryDto();
        dto.setMsg("编辑失败");

        // 采购主表
        purchaseService.updateById(purchasedtldto);
        if (purchasedtldto.getDetaillist().size() > 0){
            for (PurchaseDtl item: purchasedtldto.getDetaillist()){
                //采购商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    purchasedtlService.updateById(item);
                else{
                    item.setSourceId(purchasedtldto.getId());
                    purchasedtlService.save(item);
                }
            }
        }

        // 入库单主表
        InventoryIn existCode = inventoryInService.getOne(new LambdaQueryWrapper<InventoryIn>().eq(InventoryIn::getSourceId, purchasedtldto.getId()));
        if (existCode == null){
            existCode = new InventoryIn();
            existCode.setBillStatus(0);
            existCode.setBillType(BillType.STOCKING.getId());
            existCode.setSourceId(purchasedtldto.getId());
            existCode.setCode(billCodeBuilderService.getBillCode(BillType.STOCKING.getId()));
            inventoryInService.save(existCode);

            List<InventoryInMtl> detaillist = new ArrayList<>();
            if (purchasedtldto.getDetaillist().size() > 0){
                for (PurchaseDtl item: purchasedtldto.getDetaillist()){
                    //入库单商品详情
                    InventoryInMtl inventoryInMtl = new InventoryInMtl();
                    inventoryInMtl.setCode(billCodeBuilderService.getBillCode(BillType.STOCKING.getId()));
                    inventoryInMtl.setSourceId(existCode.getId());
                    inventoryInMtl.setMtlId(item.getMtlId());
                    inventoryInMtl.setQuantity(new BigDecimal(item.getQuantity()));
                    inventoryInMtl.setUnitId(item.getUnitId());
                    inventoryInMtlService.save(inventoryInMtl);
                    detaillist.add(inventoryInMtl);
                }
                existCode.setInventoryInMtls(detaillist);
            }
        }
        dto.setInventory(existCode);
        dto.setMsg("编辑成功");

        return Result.ok(dto);
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id){
        purchaseService.removeById(id);
        purchasedtlService.removeBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        purchaseService.removeByIds(Arrays.asList(ids.split(",")));
        purchasedtlService.removeBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id){
        Purchase purchase = purchaseService.getById(id);
        System.out.println(purchase.getId());
        if (purchase == null){
            return Result.ok("未找到对应数据");
        }
        PurchaseDtlDto purchasedtldto = new PurchaseDtlDto();
        purchasedtldto.setId(purchase.getId());
        purchasedtldto.setVendorId(purchase.getVendorId());
        purchasedtldto.setContent(purchase.getContent());
        purchasedtldto.setWarehouseId(purchase.getWarehouseId());
        purchasedtldto.setAccount(purchase.getAccount());
        purchasedtldto.setPayamount(purchase.getPayamount());
        purchasedtldto.setBilldate(purchase.getBilldate());
        purchasedtldto.setTotalamount(purchase.getTotalamount());
        purchasedtldto.setCode(purchase.getCode());
        purchasedtldto.setCreateTime(purchase.getCreateTime());
        purchasedtldto.setDetaillist(purchasedtlService.queryBySourceId(purchase.getId()));
        purchasedtldto.setInventoryOut(inventoryOutService.getOne(new LambdaQueryWrapper<InventoryOut>().eq(InventoryOut::getSourceId, purchase.getId())));

        return Result.ok(purchasedtldto);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Purchase purchase){
        return super.exportXls(request, purchase,Purchase.class, "采购列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response){
        return super.importExcel(request, response, Purchase.class);
    }













}
