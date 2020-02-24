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
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.basic.entity.Vendor;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.enums.BillStatus;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.VendorService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.entity.InventoryOutMtl;
import org.jeecg.modules.inventory.service.InventoryOutMtlService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.purchase.dto.PurchaseReturnInDto;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;
import org.jeecg.modules.purchase.entity.PurchaseReturn;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;
import org.jeecg.modules.purchase.service.PurchaseReturnMtlService;
import org.jeecg.modules.purchase.service.PurchaseReturnMtlService;
import org.jeecg.modules.purchase.service.PurchaseReturnService;
import org.jeecg.modules.purchase.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "采购退货")
@RestController
@RequestMapping("/purchasereturn")
public class PurchaseReturnController extends JeecgController<PurchaseReturn, PurchaseReturnService> {

    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseReturnService purchaseReturnService;
    @Autowired
    private PurchaseReturnMtlService purchaseReturnMtlService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private InventoryOutService inventoryOutService;

    @GetMapping("/getPaged")
    public Result<?> queryPageList(PurchaseReturn purchasereturn,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req)
    {
        QueryWrapper<PurchaseReturn> queryWrapper = QueryGenerator.initQueryWrapper(purchasereturn, req.getParameterMap());
        Page<PurchaseReturn> page = new Page<>(pageNo, pageSize);
        IPage<PurchaseReturn> pageList = purchaseReturnService.page(page, queryWrapper);
        List<PurchaseReturn> dataList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(dataList)) {
            Map<String, String> vendorNames = new HashMap<>();
            Map<String, String> warehouseNames = new HashMap<>();
            List<String> vendorIdList = dataList.stream().filter(s-> StringUtils.isNotBlank(s.getVendorId())).map(PurchaseReturn::getVendorId).collect(Collectors.toList());
            List<String> warehouseIdList = dataList.stream().filter(s->StringUtils.isNotBlank(s.getWarehouseId())).map(PurchaseReturn::getWarehouseId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(vendorIdList)) {
                Collection<Vendor> vendors = vendorService.listByIds(vendorIdList);
                vendorNames = vendors.stream().collect(Collectors.toMap(Vendor::getId, Vendor::getName));
            }
            if (CollectionUtils.isNotEmpty(warehouseIdList)) {
                Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIdList);
                warehouseNames = warehouses.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse:: getName));
            }
            for(PurchaseReturn o :dataList){
                o.setVendor(vendorNames.get(o.getVendorId()));
                o.setWarehouse(warehouseNames.get(o.getWarehouseId()));
                o.setBillStatusName(BillStatus.getName(o.getBillStatus()));
            };
            pageList.setRecords(dataList);
        }
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
            purchaseReturnService.save(rtn);

            List<PurchaseReturnMtl> list = dto.getDetaillist();
            if (list.size() > 0){
                for (PurchaseReturnMtl item:list){
                    // 退货商品表
                    PurchaseReturnMtl purchaseReturnDtl = new PurchaseReturnMtl();
                    purchaseReturnDtl.setSourceId(rtn.getId());
                    purchaseReturnDtl.setCode(code);
                    purchaseReturnDtl.setMtlId(item.getMtlId());
                    purchaseReturnDtl.setUnitId(item.getUnitId());
                    purchaseReturnDtl.setQuantity(item.getQuantity());
                    purchaseReturnDtl.setPrice(item.getPrice());
                    purchaseReturnDtl.setDiscount(item.getDiscount());
                    purchaseReturnDtl.setAmount(item.getAmount());
                    purchaseReturnMtlService.save(purchaseReturnDtl);
                }
            }
            //出库单
            String stockcode = billCodeBuilderService.getBillCode(BillType.STOREOUT.getId());
            InventoryOut inventoryOut = new InventoryOut();
            inventoryOut.setSourceId(rtn.getId());
            inventoryOut.setCode(stockcode);
            inventoryOut.setBillType(BillType.STOREOUT.getId());
            inventoryOut.setSourceBillType(BillType.PURCHASERETURNORDER.getId());
            inventoryOut.setWarehouseId(purchase.getWarehouseId());
            inventoryOut.setPutOutTime(dto.getPutOutTime());
            inventoryOut.setBillStatus(0);
            inventoryOutService.saveToInventoryOut(inventoryOut);
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
        purchaseReturnService.removeById(id);
//        purchaseService.removeById(id);
//        purchasedtlService.removeBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        purchaseReturnService.removeByIds(Arrays.asList(ids.split(",")));
//        purchaseService.removeByIds(Arrays.asList(ids.split(",")));
//        purchasedtlService.removeBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id){
        PurchaseReturn purchasereturn = purchaseReturnService.getById(id);
//        Purchase purchase = purchaseService.getById(id);
//        System.out.println(purchase.getId());
        if (purchasereturn == null){
            return Result.ok("未找到对应数据");
        }
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
