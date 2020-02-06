package org.jeecg.modules.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.service.InventoryInMtlService;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.purchase.service.PurchaseMtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "入库单")
@RestController
@RequestMapping("/inventoryIn")
public class InventoryInController {

    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryInMtlService inventoryInMtlService;
    @Autowired
    private PurchaseMtlService purchaseMtlService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialUnitService materialUnitService;
    /**
     * 添加
     *
     * @param inventoryIn
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加入库单")
    @ApiOperation(value = "添加入库单", notes = "添加入库单")
    public Result<?> add(@RequestBody InventoryIn inventoryIn) {
        if (StringUtils.isEmpty(inventoryIn.getId())) {
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
        }
        InventoryIn existCode = inventoryInService.getOne(new LambdaQueryWrapper<InventoryIn>().eq(InventoryIn::getCode, inventoryIn.getCode()).ne(InventoryIn::getId, inventoryIn.getId()));
        Assert.isNull(existCode, "单号已存在！");
        inventoryInService.save(inventoryIn);
        Result<Object> result = Result.ok();
        result.setResult(inventoryIn);
        return result;
    }
    /**
     * 获取所有数据
     *
     * @param inventoryIn
     * @param req
     * @return
     */
    @ApiOperation(value = "获取入库单数据", notes = "获取所有入库单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(InventoryIn inventoryIn, HttpServletRequest req) {
        QueryWrapper<InventoryIn> queryWrapper = QueryGenerator.initQueryWrapper(inventoryIn, req.getParameterMap());
        List<InventoryIn> list = inventoryInService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param inventoryIn
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取入库单数据列表", notes = "获取所有入库单数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(InventoryIn inventoryIn, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<InventoryIn> queryWrapper = QueryGenerator.initQueryWrapper(inventoryIn, req.getParameterMap());
        Page<InventoryIn> page = new Page<InventoryIn>(pageNo, pageSize);
        queryWrapper.eq("row_sts", RowSts.EFFECTIVE.getId());
        IPage<InventoryIn> pageList = inventoryInService.page(page, queryWrapper);
        List<InventoryIn> inventoryInList = pageList.getRecords();
        List<String> warehouseIds = inventoryInList.stream().map(InventoryIn::getWarehouseId).collect(Collectors.toList());
        Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
        Map<String, String> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse:: getId, Warehouse:: getName));
        inventoryInList.stream().forEach(o->{
            o.setWarehouse(warehouseMap.get(o.getWarehouseId()));
        });

        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 修改
     *
     * @param inventoryIn
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改入库单")
    @ApiOperation(value = "修改入库单", notes = "修改入库单")
    public Result<?> edit(@RequestBody InventoryIn inventoryIn){
        inventoryInService.updateById(inventoryIn);
        Result<Object> result = Result.ok();
        result.setResult(inventoryIn);
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除入库单")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除入库单", notes = "通过ID删除入库单")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        inventoryInService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除入库单", notes = "批量删除入库单")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.inventoryInService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询入库单", notes = "通过ID查询入库单")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        InventoryIn inventoryIn = inventoryInService.getById(id);
        return Result.ok(inventoryIn);
    }

    /**
     * 获取待入库产品列表
     *
     * @param id
     * @param sourceId
     * @return
     */
    @ApiOperation(value = "获取采购订单待入库产品列表", notes = "获取采购订单待入库产品列表")
    @GetMapping(value = "/mtl/getList")
    public Result<?> getList(@ApiParam(name = "id", value = "出库单信息ID", required = true) @RequestParam(name = "id", required = true) String id,
                             @ApiParam(name = "sourceId", value = "原单id", required = true) @RequestParam(name = "sourceId", required = true) String sourceId) {
        List<PreInventoryOutMtl> list = inventoryInService.getDeliveryMtlList(id, sourceId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> mtlIds = list.stream().map(PreInventoryOutMtl::getMtlId).collect(Collectors.toList());
            List<String> unitIds = list.stream().map(PreInventoryOutMtl::getUnitId).collect(Collectors.toList());
            Collection<Material> materials = materialService.listByIds(mtlIds);
            Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
            Map<String, String> mtlNameMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
            Map<String, String> mtlCodeMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getCode));
            Map<String, String> mtlSpectxMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getSpecification));
            Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
            list.stream().forEach(o->{
                o.setUnit(unitMap.get(o.getUnitId()));
                o.setMtl(mtlNameMap.get(o.getMtlId()));
                o.setMtlCode(mtlCodeMap.get(o.getMtlId()));
                o.setSpecification(mtlSpectxMap.get(o.getMtlId()));
            });
        }
        return Result.ok(list);
    }

    /**
     * 修改
     *
     * @param mtls
     * @return
     */
    @PostMapping(value = "/mtls/stockin")
    @AutoLog(value = "修改采购订单发货信息")
    @ApiOperation(value = "修改采购订单发货信息", notes = "修改采购订单发货信息")
    public Result<?> stockin(@RequestBody List<PreInventoryOutMtl> mtls){
        if (CollectionUtils.isNotEmpty(mtls)) {
            inventoryInService.stockIn(mtls);
        }
        return Result.ok();
    }
}
