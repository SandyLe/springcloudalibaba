package org.jeecg.modules.purchase.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.purchase.dto.PurchaseInventoryDto;
import org.jeecg.modules.purchase.dto.PurchaseDtlDto;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.PurchaseMtl;
import org.jeecg.modules.purchase.service.PurchaseService;
import org.jeecg.modules.purchase.service.PurchaseMtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "采购列表")
@RestController
@RequestMapping("/purchase")
public class PurchaseController extends JeecgController<Purchase, PurchaseService> {
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseMtlService purchaseMtlService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(Purchase purchase,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Purchase> queryWrapper = QueryGenerator.initQueryWrapper(purchase, req.getParameterMap());
        Page<Purchase> page = new Page<>(pageNo, pageSize);
        IPage<Purchase> pageList = purchaseService.page(page, queryWrapper);
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

        //采购单子表
        List<InventoryInMtl> detaillist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(purchasedtldto.getDetaillist())){
            List<PurchaseMtl> mtls = purchasedtldto.getDetaillist().stream().filter(o->StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //采购商品详情
                o.setCode(code);
                o.setSourceId(purchasedtldto.getId());
            });
            purchaseMtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(purchasedtldto.getWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(purchasedtldto.getWarehouseId());
            inventoryIn.setPutInTime(purchasedtldto.getPutInTime());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(purchasedtldto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.PURCHASEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        dto.setMsg("添加成功");
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
            for (PurchaseMtl item: purchasedtldto.getDetaillist()){
                //采购商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    purchaseMtlService.updateById(item);
                else{
                    item.setSourceId(purchasedtldto.getId());
                    purchaseMtlService.save(item);
                }
            }
        }

        // 入库单主表
        inventoryInService.deleteBySourceId(purchasedtldto.getId());

        if (StringUtils.isNotBlank(purchasedtldto.getWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(purchasedtldto.getWarehouseId());
            inventoryIn.setPutInTime(purchasedtldto.getPutInTime());
            inventoryIn.setSourceCode(purchasedtldto.getCode());
            inventoryIn.setSourceId(purchasedtldto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.PURCHASEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
            dto.setInventory(inventoryIn);
        }
        dto.setMsg("编辑成功");

        return Result.ok(dto);
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id){
        purchaseService.removeById(id);
        purchaseMtlService.removeBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        purchaseService.removeByIds(Arrays.asList(ids.split(",")));
        purchaseMtlService.removeBySourceIds(Arrays.asList(ids.split(",")));
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
        purchasedtldto.setBatchNo(purchase.getBatchNo());
        purchasedtldto.setDetaillist(purchaseMtlService.queryBySourceId(purchase.getId()));

        return Result.ok(purchasedtldto);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code){

        LambdaQueryWrapper<Purchase> lambdaQueryWrapper = new LambdaQueryWrapper<Purchase>().eq(Purchase::getCode, code);
        Purchase purchase = purchaseService.getOne(lambdaQueryWrapper);
        if (purchase == null){
            return Result.ok("未找到对应数据");
        }
        return Result.ok(purchase);
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
