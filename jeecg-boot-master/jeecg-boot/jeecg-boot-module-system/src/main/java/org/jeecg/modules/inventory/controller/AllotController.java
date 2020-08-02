package org.jeecg.modules.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.inventory.entity.*;
import org.jeecg.modules.inventory.service.*;
import org.jeecg.modules.inventory.dto.AllotDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "调拨列表")
@RestController
@RequestMapping("/allot")
public class AllotController extends JeecgController<Allot, AllotService> {
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private AllotService allotService;
    @Autowired
    private AllotDtlService allotDtlService;
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(Allot allot,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Allot> queryWrapper = QueryGenerator.initQueryWrapper(allot, req.getParameterMap());
        Page<Allot> page = new Page<>(pageNo, pageSize);
        IPage<Allot> pageList = allotService.page(page, queryWrapper);
        List<Allot> datas = pageList.getRecords();
        List<String> fromWarehoseIds = datas.stream().map(Allot::getFromWarehouseId).collect(Collectors.toList());
        List<String> toWarehouseIds = datas.stream().map(Allot::getToWarehouseId).collect(Collectors.toList());
        fromWarehoseIds.addAll(toWarehouseIds);
        List<String> warehouseIds = fromWarehoseIds.stream().distinct().collect(Collectors.toList());
        Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
        Map<String, String> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
        datas.stream().forEach(o -> {
            o.setFromWarehouse(warehouseMap.get(o.getFromWarehouseId()));
            o.setToWarehouse(warehouseMap.get(o.getToWarehouseId()));
        });
        pageList.setRecords(datas);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody AllotDto allotdto) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(allotdto.getCompanyId())) {
            allotdto.setCompanyId(sysUser.getCompanyId());
        }
        if (null == allotdto.getBillType()) {
            allotdto.setBillType(BillType.ALLOT.getId());
        }
        String resultId = allotService.saveOrder(allotdto);
        Result<Object> result = Result.ok();
        result.setResult(resultId);
        return result;
    }

    @PutMapping("/edit")
    @Transactional
    public Result<?> edit(@RequestBody AllotDto allotdto) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(allotdto.getCompanyId())) {
            allotdto.setCompanyId(sysUser.getCompanyId());
        }
        if (null == allotdto.getBillType()) {
            allotdto.setBillType(BillType.ALLOT.getId());
        }
        allotService.editOrder(allotdto);

        return Result.ok();
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        allotService.removeById(id);
        allotDtlService.deleteBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        allotService.removeByIds(Arrays.asList(ids.split(",")));
        allotDtlService.deleteBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/getOne")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Allot allot = allotService.getById(id);
        if (allot == null) {
            return Result.ok("未找到对应数据");
        }
        AllotDto allotdto = new AllotDto();
        allotdto.setId(allot.getId());
        allotdto.setContent(allot.getContent());
        allotdto.setFromWarehouseId(allot.getFromWarehouseId());
        allotdto.setToWarehouseId(allot.getToWarehouseId());
        allotdto.setBilldate(allot.getBilldate());
        allotdto.setCode(allot.getCode());
        allotdto.setCreateTime(allot.getCreateTime());
        allotdto.setCompanyId(allot.getCompanyId());
        List<AllotDtl> allotDtls = allotDtlService.findBySourceId(allot.getId());
        allotDtls.stream().forEach(o->{
            Inventory fromInventory = inventoryService.findInventory(allotdto.getFromWarehouseId(), o.getMtlId(), o.getUnitId());
            if (null != fromInventory){
                o.setFromAmount(fromInventory.getStockAmount());
            }
            Inventory toInventory = inventoryService.findInventory(allotdto.getToWarehouseId(), o.getMtlId(), o.getUnitId());
            if (null != toInventory){
                o.setToAmount(toInventory.getStockAmount());
            }
        });
        allotdto.setDetaillist(allotDtls);

        return Result.ok(allotdto);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code) {

        LambdaQueryWrapper<Allot> lambdaQueryWrapper = new LambdaQueryWrapper<Allot>().eq(Allot::getCode, code);
        Allot allot = allotService.getOne(lambdaQueryWrapper);
        if (allot == null) {
            return Result.ok("未找到对应数据");
        }
        return Result.ok(allot);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Allot allot) {
        return super.exportXls(request, allot, Allot.class, "拆卸单列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Allot.class);
    }
}
