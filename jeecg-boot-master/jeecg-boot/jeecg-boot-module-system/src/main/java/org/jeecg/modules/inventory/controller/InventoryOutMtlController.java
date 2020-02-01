package org.jeecg.modules.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.jeecg.modules.inventory.entity.InventoryOutMtl;
import org.jeecg.modules.inventory.service.InventoryOutMtlService;
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
@Api(tags = "出库单")
@RestController
@RequestMapping("/inventoryOutMtl")
public class InventoryOutMtlController {

    @Autowired
    private InventoryOutMtlService inventoryOutMtlService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialUnitService materialUnitService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    /**
     * 添加
     *
     * @param inventoryOutMtl
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加出库单")
    @ApiOperation(value = "添加出库单", notes = "添加出库单")
    public Result<?> add(@RequestBody InventoryOutMtl inventoryOutMtl) {
        if (StringUtils.isEmpty(inventoryOutMtl.getId())) {
            inventoryOutMtl.setCode(billCodeBuilderService.getBillCode(BillType.STOREOUT.getId()));
        }
        InventoryOutMtl existCode = inventoryOutMtlService.getOne(new LambdaQueryWrapper<InventoryOutMtl>().eq(InventoryOutMtl::getCode, inventoryOutMtl.getCode()).ne(InventoryOutMtl::getId, inventoryOutMtl.getId()));
        Assert.isNull(existCode, "单号已存在！");
        inventoryOutMtlService.save(inventoryOutMtl);
        Result<Object> result = Result.ok();
        result.setResult(inventoryOutMtl);
        return result;
    }
    /**
     * 获取所有数据
     *
     * @param inventoryOutMtl
     * @param req
     * @return
     */
    @ApiOperation(value = "获取出库单数据", notes = "获取所有出库单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(InventoryOutMtl inventoryOutMtl, HttpServletRequest req) {
        QueryWrapper<InventoryOutMtl> queryWrapper = QueryGenerator.initQueryWrapper(inventoryOutMtl, req.getParameterMap());
        List<InventoryOutMtl> list = inventoryOutMtlService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param inventoryOutMtl
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取出库单数据列表", notes = "获取所有出库单数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(InventoryOutMtl inventoryOutMtl, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<InventoryOutMtl> queryWrapper = QueryGenerator.initQueryWrapper(inventoryOutMtl, req.getParameterMap());
        Page<InventoryOutMtl> page = new Page<InventoryOutMtl>(pageNo, pageSize);

        IPage<InventoryOutMtl> pageList = inventoryOutMtlService.page(page, queryWrapper);
        List<InventoryOutMtl> inventoryOutMtlList = pageList.getRecords();
        List<String> mtlIds = inventoryOutMtlList.stream().map(InventoryOutMtl::getMtlId).collect(Collectors.toList());
        List<String> unitIds = inventoryOutMtlList.stream().map(InventoryOutMtl::getUnitId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
        Map<String, String> materialMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        Map<String, String> mtlCodeMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getCode));
        Map<String, String> specMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getSpecification));
        Map<String, String> barCodeMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getBarCode));
        Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
        inventoryOutMtlList.stream().forEach(o->{
            o.setMaterial(materialMap.get(o.getMtlId()));
            o.setUnit(unitMap.get(o.getUnitId()));
            o.setBarCode(barCodeMap.get(o.getMtlId()));
            o.setSpecification(specMap.get(o.getMtlId()));
            o.setMtlCode(mtlCodeMap.get(o.getMtlId()));
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
     * @param inventoryOutMtl
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改出库单")
    @ApiOperation(value = "修改出库单", notes = "修改出库单")
    public Result<?> edit(@RequestBody InventoryOutMtl inventoryOutMtl){
        inventoryOutMtlService.updateById(inventoryOutMtl);
        Result<Object> result = Result.ok();
        result.setResult(inventoryOutMtl);
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除出库单")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除出库单", notes = "通过ID删除出库单")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        inventoryOutMtlService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除出库单", notes = "批量删除出库单")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.inventoryOutMtlService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询出库单", notes = "通过ID查询出库单")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        InventoryOutMtl inventoryOutMtl = inventoryOutMtlService.getById(id);
        return Result.ok(inventoryOutMtl);
    }

}
