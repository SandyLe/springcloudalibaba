package org.jeecg.modules.stocking.controller;

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
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.common.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.stocking.entity.Stocking;
import org.jeecg.modules.stocking.service.StockingService;
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
@Api(tags = "单表盘点单")
@RestController
@RequestMapping("/stocking")
public class StockingController {

    @Autowired
    private StockingService stockingService;
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
     * @param stocking
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加盘点单")
    @ApiOperation(value = "添加盘点单", notes = "添加盘点单")
    public Result<?> add(@RequestBody Stocking stocking) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(stocking.getCompanyId())) {
            stocking.setCompanyId(sysUser.getCompanyId());
        }
        if (StringUtils.isEmpty(stocking.getId())) {
            stocking.setCode(billCodeBuilderService.getBillCode(BillType.STOCKING.getId()));
        }
        Stocking existCode = stockingService.getOne(new LambdaQueryWrapper<Stocking>().eq(Stocking::getCode, stocking.getCode()).ne(Stocking::getId, stocking.getId()));
        Assert.isNull(existCode, "单号已存在！");
        stockingService.save(stocking);
        Result<Object> result = Result.ok();
        result.setResult(stocking);
        return result;
    }
    /**
     * 获取所有数据
     *
     * @param stocking
     * @param req
     * @return
     */
    @ApiOperation(value = "获取盘点单数据", notes = "获取所有盘点单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(Stocking stocking, HttpServletRequest req) {
        QueryWrapper<Stocking> queryWrapper = QueryGenerator.initQueryWrapper(stocking, req.getParameterMap());
        List<Stocking> list = stockingService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param stocking
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取盘点单数据列表", notes = "获取所有盘点单数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(Stocking stocking, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Stocking> queryWrapper = QueryGenerator.initQueryWrapper(stocking, req.getParameterMap());
        Page<Stocking> page = new Page<Stocking>(pageNo, pageSize);

        IPage<Stocking> pageList = stockingService.page(page, queryWrapper);
        List<Stocking> stockingList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(stockingList)) {
            List<String> mtlIds = stockingList.stream().map(Stocking::getMtlId).collect(Collectors.toList());
            List<String> warehouseIds = stockingList.stream().map(Stocking::getWarehouseId).collect(Collectors.toList());
            List<String> unitIds = stockingList.stream().map(Stocking::getUnitId).collect(Collectors.toList());
            Collection<Material> materials = materialService.listByIds(mtlIds);
            Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
            Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
            Map<String, String> materialMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
            Map<String, String> mtlCodeMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getCode));
            Map<String, String> specMap = materials.stream().filter(o->StringUtils.isNotBlank(o.getSpecification())).collect(Collectors.toMap(Material::getId, Material::getSpecification));
            Map<String, String> barCodeMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getBarCode));
            Map<String, String> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse:: getId, Warehouse:: getName));
            Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
            stockingList.stream().forEach(o->{
                o.setWarehouse(warehouseMap.get(o.getWarehouseId()));
                o.setMaterial(materialMap.get(o.getMtlId()));
                o.setUnit(unitMap.get(o.getUnitId()));
                o.setBarCode(barCodeMap.get(o.getMtlId()));
                o.setSpecification(specMap.get(o.getMtlId()));
                o.setMtlCode(mtlCodeMap.get(o.getMtlId()));
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
     * @param stocking
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改盘点单")
    @ApiOperation(value = "修改盘点单", notes = "修改盘点单")
    public Result<?> edit(@RequestBody Stocking stocking){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(stocking.getCompanyId())) {
            stocking.setCompanyId(sysUser.getCompanyId());
        }
        stockingService.updateById(stocking);
        Result<Object> result = Result.ok();
        result.setResult(stocking);
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除盘点单")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除盘点单", notes = "通过ID删除盘点单")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        stockingService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除盘点单", notes = "批量删除盘点单")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.stockingService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询盘点单", notes = "通过ID查询盘点单")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        Stocking stocking = stockingService.getById(id);
        return Result.ok(stocking);
    }

    /**
     * 盘点
     *
     * @param stocking
     * @return
     */
    @PostMapping(value = "/handleStocking")
    @ApiOperation(value = "通过ID查询盘点单", notes = "通过ID查询盘点单")
    public Result<?> handleStocking(@RequestBody Stocking stocking) {
        String msg = stockingService.handleStocking(stocking.getId());
        return Result.ok("操作成功！");
    }

}
