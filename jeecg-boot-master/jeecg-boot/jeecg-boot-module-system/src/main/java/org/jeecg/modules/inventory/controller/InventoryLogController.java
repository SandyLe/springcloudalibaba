package org.jeecg.modules.inventoryLog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.inventory.entity.InventoryLog;
import org.jeecg.modules.inventory.service.InventoryLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "单表库存日志")
@RestController
@RequestMapping("/inventoryLog")
public class InventoryLogController {

    @Autowired
    private InventoryLogService inventoryLogService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private MaterialUnitService materialUnitService;
    /**
     * 添加
     *
     * @param inventoryLog
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加库存日志")
    @ApiOperation(value = "添加库存日志", notes = "添加库存日志")
    public Result<?> add(@RequestBody InventoryLog inventoryLog) {

        inventoryLogService.save(inventoryLog);
        Result<Object> result = Result.ok();
        result.setResult(inventoryLog);
        return result;
    }
    /**
     * 获取所有数据
     *
     * @param inventoryLog
     * @param req
     * @return
     */
    @ApiOperation(value = "获取库存日志数据", notes = "获取所有库存日志数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(InventoryLog inventoryLog, HttpServletRequest req) {
        QueryWrapper<InventoryLog> queryWrapper = QueryGenerator.initQueryWrapper(inventoryLog, req.getParameterMap());
        List<InventoryLog> list = inventoryLogService.list(queryWrapper);
        List<String> mtlIds = list.stream().map(InventoryLog::getMtlId).collect(Collectors.toList());
        List<String> warehouseIds = list.stream().map(InventoryLog::getWarehouseId).collect(Collectors.toList());
        List<String> unitIds = list.stream().map(InventoryLog::getUnitId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
        Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
        Map<String, String> materialMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        Map<String, String> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse:: getId, Warehouse:: getName));
        Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
        list.stream().forEach(o->{
            o.setWarehouse(warehouseMap.get(o.getWarehouseId()));
            o.setMaterial(materialMap.get(o.getMtlId()));
            o.setUnit(unitMap.get(o.getUnitId()));
        });
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param inventoryLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取库存日志数据列表", notes = "获取所有库存日志数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(InventoryLog inventoryLog, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<InventoryLog> queryWrapper = QueryGenerator.initQueryWrapper(inventoryLog, req.getParameterMap());
        Page<InventoryLog> page = new Page<InventoryLog>(pageNo, pageSize);

        IPage<InventoryLog> pageList = inventoryLogService.page(page, queryWrapper);
        List<InventoryLog> inventoryLogList = pageList.getRecords();
        List<String> mtlIds = inventoryLogList.stream().map(InventoryLog::getMtlId).collect(Collectors.toList());
        List<String> warehouseIds = inventoryLogList.stream().map(InventoryLog::getWarehouseId).collect(Collectors.toList());
        List<String> unitIds = inventoryLogList.stream().map(InventoryLog::getUnitId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
        Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
        Map<String, String> materialMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        Map<String, String> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse:: getId, Warehouse:: getName));
        Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
        inventoryLogList.stream().forEach(o->{
            o.setWarehouse(warehouseMap.get(o.getWarehouseId()));
            o.setMaterial(materialMap.get(o.getMtlId()));
            o.setUnit(unitMap.get(o.getUnitId()));
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
     * @param inventoryLog
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改库存日志")
    @ApiOperation(value = "修改库存日志", notes = "修改库存日志")
    public Result<?> edit(@RequestBody InventoryLog inventoryLog){
        inventoryLogService.updateById(inventoryLog);
        Result<Object> result = Result.ok();
        result.setResult(inventoryLog);
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除库存日志")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除库存日志", notes = "通过ID删除库存日志")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        inventoryLogService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除库存日志", notes = "批量删除库存日志")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.inventoryLogService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询库存日志", notes = "通过ID查询库存日志")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        InventoryLog inventoryLog = inventoryLogService.getById(id);
        return Result.ok(inventoryLog);
    }


}
