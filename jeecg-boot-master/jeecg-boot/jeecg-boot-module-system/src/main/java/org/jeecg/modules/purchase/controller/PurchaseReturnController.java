package org.jeecg.modules.purchase.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.basic.entity.Vendor;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.VendorService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.purchase.dto.PurchaseInventoryDto;
import org.jeecg.modules.purchase.dto.PurchaseReturnInDto;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;
import org.jeecg.modules.purchase.entity.PurchaseReturn;
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
@Api(tags = "????????????")
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
            //?????????
            PurchaseReturn rtn = new PurchaseReturn();
            String code = billCodeBuilderService.getBillCode(BillType.PURCHASERETURNORDER.getId());
            rtn.setCode(code);
            rtn.setWarehouseId(purchase.getWarehouseId());
            rtn.setAmount(dto.getAmount());
            rtn.setSourceId(purchase.getId());
            rtn.setSourceCode(purchase.getCode());
            rtn.setBilldate(DateUtils.getDate());
            rtn.setVendorId(purchase.getVendorId());
            rtn.setBillStatus(BillStatus.NEW.getId());
            rtn.setAccount(purchase.getAccount());
            rtn.setBillType(BillType.PURCHASERETURNORDER.getId());
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if (StringUtils.isBlank(rtn.getCompanyId())) {
                rtn.setCompanyId(sysUser.getCompanyId());
            }
            purchaseReturnService.save(rtn);

            List<PurchaseReturnMtl> list = dto.getDetaillist();
            if (list.size() > 0){
                for (PurchaseReturnMtl item:list){
                    // ???????????????
                    PurchaseReturnMtl purchaseReturnDtl = new PurchaseReturnMtl();
                    purchaseReturnDtl.setCompanyId(rtn.getCompanyId());
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
            //?????????
            String stockcode = billCodeBuilderService.getBillCode(BillType.INVENTORYOUT.getId());
            InventoryOut inventoryOut = new InventoryOut();
            inventoryOut.setCompanyId(rtn.getCompanyId());
            inventoryOut.setSourceId(rtn.getId());
            inventoryOut.setCode(stockcode);
            inventoryOut.setSourceCode(code);
            inventoryOut.setBillType(BillType.INVENTORYOUT.getId());
            inventoryOut.setSourceBillType(BillType.PURCHASERETURNORDER.getId());
            inventoryOut.setWarehouseId(purchase.getWarehouseId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setBillStatus(BillStatus.TOSEND.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return Result.ok("????????????");
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody PurchaseReturnInDto purchasedtldto){
        PurchaseInventoryDto dto = new PurchaseInventoryDto();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(purchasedtldto.getCompanyId())) {
            purchasedtldto.setCompanyId(sysUser.getCompanyId());
        }
        if (null == purchasedtldto.getBillType()) {
            purchasedtldto.setBillType(BillType.PURCHASERETURNORDER.getId());
        }
        dto.setMsg("????????????");

        // ??????????????????
        purchaseReturnService.updateById(purchasedtldto);
        if (purchasedtldto.getDetaillist().size() > 0){
            for (PurchaseReturnMtl item: purchasedtldto.getDetaillist()){
                item.setCompanyId(purchasedtldto.getCompanyId());
                //??????????????????
                if (item.getId() != null && item.getId().length() > 0)
                    purchaseReturnMtlService.updateById(item);
                else{
                    item.setSourceId(purchasedtldto.getId());
                    purchaseReturnMtlService.save(item);
                }
            }
        }

        // ???????????????
        inventoryOutService.deleteBySourceId(purchasedtldto.getBillType(), purchasedtldto.getId());

        if (StringUtils.isNotBlank(purchasedtldto.getWarehouseId())) {

            // ???????????????
            InventoryOut inventoryOut = new InventoryOut();
            inventoryOut.setCompanyId(purchasedtldto.getCompanyId());
            inventoryOut.setBillStatus(BillStatus.TOSEND.getId());
            inventoryOut.setWarehouseId(purchasedtldto.getWarehouseId());
            inventoryOut.setSourceCode(purchasedtldto.getCode());
            inventoryOut.setSourceId(purchasedtldto.getId());
            inventoryOut.setBillType(BillType.INVENTORYOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setSourceBillType(BillType.PURCHASERETURNORDER.getId());
            inventoryOut.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYOUT.getId()));
            inventoryOutService.saveToInventoryOut(inventoryOut);
            dto.setInventoryOut(inventoryOut);
        }
        dto.setMsg("????????????");

        return Result.ok(dto);
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id){
        purchaseReturnService.removeById(id);
//        purchaseService.removeById(id);
//        purchasedtlService.removeBySourceId(id);
        return Result.ok("????????????!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        purchaseReturnService.removeByIds(Arrays.asList(ids.split(",")));
//        purchaseService.removeByIds(Arrays.asList(ids.split(",")));
//        purchasedtlService.removeBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("??????????????????!");
    }

    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id){
        PurchaseReturn purchasereturn = purchaseReturnService.getById(id);
        if (purchasereturn == null){
            return Result.ok("?????????????????????");
        }
        PurchaseReturnInDto purchasedtldto = new PurchaseReturnInDto();
        purchasedtldto.setSourceId(purchasereturn.getSourceId());
        purchasedtldto.setSourceCode(purchasereturn.getSourceCode());
        purchasedtldto.setId(purchasereturn.getId());
        purchasedtldto.setVendorId(purchasereturn.getVendorId());
        purchasedtldto.setContent(purchasereturn.getContent());
        purchasedtldto.setWarehouseId(purchasereturn.getWarehouseId());
        purchasedtldto.setAccount(purchasereturn.getAccount());
        purchasedtldto.setBilldate(purchasereturn.getBilldate());
        purchasedtldto.setAmount(purchasereturn.getAmount());
        purchasedtldto.setCode(purchasereturn.getCode());
        purchasedtldto.setCreateTime(purchasereturn.getCreateTime());
        purchasedtldto.setCompanyId(purchasereturn.getCompanyId());
        purchasedtldto.setDetaillist(purchaseReturnMtlService.queryBySourceId(purchasereturn.getId()));

        return Result.ok(purchasedtldto);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurchaseReturn purchasereturn){
        return super.exportXls(request, purchasereturn, PurchaseReturn.class, "????????????");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response){
        return super.importExcel(request, response, PurchaseReturn.class);
    }













}
