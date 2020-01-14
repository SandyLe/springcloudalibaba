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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.inventory.entity.Inventory;
import org.jeecg.modules.inventory.service.InventoryService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "单表库存")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private MaterialUnitService materialUnitService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    /**
     * 添加
     *
     * @param inventory
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加库存")
    @ApiOperation(value = "添加库存", notes = "添加库存")
    public Result<?> add(@RequestBody Inventory inventory) {
        if (StringUtils.isEmpty(inventory.getId())) {
            inventory.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORY.getId()));
        }
        Inventory existCode = inventoryService.getOne(new LambdaQueryWrapper<Inventory>().eq(Inventory::getCode, inventory.getCode()).ne(Inventory::getId, inventory.getId()));
        Assert.isNull(existCode, "单号已存在！");
        inventoryService.save(inventory);
        Result<Object> result = Result.ok();
        result.setResult(inventory);
        return result;
    }
    /**
     * 获取所有数据
     *
     * @param inventory
     * @param req
     * @return
     */
    @ApiOperation(value = "获取库存数据", notes = "获取所有库存数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(Inventory inventory, HttpServletRequest req) {
        QueryWrapper<Inventory> queryWrapper = QueryGenerator.initQueryWrapper(inventory, req.getParameterMap());
        List<Inventory> list = inventoryService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param inventory
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取库存数据列表", notes = "获取所有库存数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(Inventory inventory, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Inventory> queryWrapper = QueryGenerator.initQueryWrapper(inventory, req.getParameterMap());
        Page<Inventory> page = new Page<Inventory>(pageNo, pageSize);

        IPage<Inventory> pageList = inventoryService.page(page, queryWrapper);
        List<Inventory> inventoryList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(inventoryList)) {
            List<String> mtlIds = inventoryList.stream().map(Inventory::getMtlId).collect(Collectors.toList());
            List<String> warehouseIds = inventoryList.stream().map(Inventory::getWarehouseId).collect(Collectors.toList());
            List<String> unitIds = inventoryList.stream().map(Inventory::getUnitId).collect(Collectors.toList());
            Collection<Material> materials = materialService.listByIds(mtlIds);
            Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
            Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
            Map<String, String> materialMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
            Map<String, String> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse:: getId, Warehouse:: getName));
            Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
            inventoryList.stream().forEach(o->{
                o.setWarehouse(warehouseMap.get(o.getWarehouseId()));
                o.setMaterial(materialMap.get(o.getMtlId()));
                o.setUnit(unitMap.get(o.getUnitId()));
            });
        }

        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 修改
     *
     * @param inventory
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改库存")
    @ApiOperation(value = "修改库存", notes = "修改库存")
    public Result<?> edit(@RequestBody Inventory inventory){
        Inventory existCode = inventoryService.getOne(new LambdaQueryWrapper<Inventory>().eq(Inventory::getCode, inventory.getCode()).ne(Inventory::getId, inventory.getId()));
        Assert.isNull(existCode, "单号已存在！");
        inventoryService.updateById(inventory);
        Result<Object> result = Result.ok();
        result.setResult(inventory);
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除库存")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除库存", notes = "通过ID删除库存")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        inventoryService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除库存", notes = "批量删除库存")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.inventoryService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询库存", notes = "通过ID查询库存")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        Inventory inventory = inventoryService.getById(id);
        return Result.ok(inventory);
    }


}
