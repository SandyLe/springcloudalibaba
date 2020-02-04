package org.jeecg.modules.saleorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.enums.AbstractEnum;
import org.jeecg.modules.basic.enums.DiscountType;
import org.jeecg.modules.basic.enums.EnumConvertUtils;
import org.jeecg.modules.basic.enums.RowSts;
import org.jeecg.modules.basic.service.CustomerService;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "单表销售订单")
@RestController
@RequestMapping("/saleOrderMtl")
public class SaleOrderMtlController {

    @Autowired
    private SaleOrderMtlService saleOrderMtlService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialUnitService materialUnitService;
    /**
     * 添加
     *
     * @param saleOrderMtl
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加销售订单")
    @ApiOperation(value = "添加销售订单", notes = "添加销售订单")
    public Result<?> add(@RequestBody SaleOrderMtl saleOrderMtl) {
        if (null == saleOrderMtl.getRowSts()) {
            saleOrderMtl.setRowSts(RowSts.EFFECTIVE.getId());
        }
        saleOrderMtlService.saveSaleOrderMtl(saleOrderMtl);
        return Result.ok("添加成功！");
    }
    /**
     * 获取所有数据
     *
     * @param saleOrderMtl
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单数据", notes = "获取所有销售订单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrderMtl saleOrderMtl, HttpServletRequest req) {
        QueryWrapper<SaleOrderMtl> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderMtl, req.getParameterMap());
        List<SaleOrderMtl> saleOrderMtlList = saleOrderMtlService.list(queryWrapper);
        List<String> mtlIds = saleOrderMtlList.stream().map(SaleOrderMtl::getMtlId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        Map<String, String> mtlCodeMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getCode));
        Map<String, String> mtlSpecMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getSpecification));
        saleOrderMtlList.stream().forEach(o->{
            o.setMtl(mtlMap.get(o.getMtlId()));
            o.setMtlCode(mtlCodeMap.get(o.getMtlId()));
            o.setSpecification(mtlSpecMap.get(o.getMtlId()));
        });
        return Result.ok(saleOrderMtlList);
    }
    /**
     * 分页列表查询
     *
     * @param saleOrderMtl
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单数据列表", notes = "获取所有销售订单数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(SaleOrderMtl saleOrderMtl, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {

        QueryWrapper<SaleOrderMtl> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderMtl, req.getParameterMap());
        Page<SaleOrderMtl> page = new Page<SaleOrderMtl>(pageNo, pageSize);

        IPage<SaleOrderMtl> pageList = saleOrderMtlService.page(page, queryWrapper);
        List<SaleOrderMtl> saleOrderList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(saleOrderList)) {
            List<String> mtlIds = saleOrderList.stream().map(SaleOrderMtl::getMtlId).collect(Collectors.toList());
            List<String> unitIds = saleOrderList.stream().map(SaleOrderMtl::getUnitId).collect(Collectors.toList());
            Collection<Material> materials = materialService.listByIds(mtlIds);
            Collection<MaterialUnit> warehouses = materialUnitService.listByIds(unitIds);
            Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
            Map<String, String> mtlCodeMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getCode));
            Map<String, String> mtlSpecMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getSpecification));
            Map<String, String> unitMap = warehouses.stream().collect(Collectors.toMap(MaterialUnit:: getId, MaterialUnit:: getName));
            saleOrderList.stream().forEach(o->{
                o.setUnit(unitMap.get(o.getUnitId()));
                o.setMtl(mtlMap.get(o.getMtlId()));
                o.setMtlCode(mtlCodeMap.get(o.getMtlId()));
                o.setSpecification(mtlSpecMap.get(o.getMtlId()));
                o.setDiscountTypeName(EnumConvertUtils.getName(DiscountType.class, o.getDiscountType()));
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
     * @param saleOrderMtl
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改销售订单")
    @ApiOperation(value = "修改销售订单", notes = "修改销售订单")
    public Result<?> edit(@RequestBody SaleOrderMtl saleOrderMtl){
        saleOrderMtlService.saveSaleOrderMtl(saleOrderMtl);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除销售订单")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除销售订单", notes = "通过ID删除销售订单")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        saleOrderMtlService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除销售订单", notes = "批量删除销售订单")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.saleOrderMtlService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询销售订单", notes = "通过ID查询销售订单")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        SaleOrderMtl saleOrderMtl = saleOrderMtlService.getById(id);
        return Result.ok(saleOrderMtl);
    }

    /**
     * 通过mtlId查询
     *
     * @param mtlId
     * @return
     */
    @GetMapping(value = "/mtl/getOne")
    @ApiOperation(value = "通过ID查询销售订单", notes = "通过ID查询销售订单")
    public Result<?> queryById(@ApiParam(name = "mtlId", value = "mtlId", required = true) @RequestParam(name = "mtlId", required = true) String mtlId,
                               @ApiParam(name = "sourceId", value = "sourceId", required = true) @RequestParam(name = "sourceId", required = true) String sourceId) {
        LambdaQueryWrapper<SaleOrderMtl> lambdaQueryWrapper = new LambdaQueryWrapper<SaleOrderMtl>().eq(SaleOrderMtl::getSourceId, sourceId)
                .eq(SaleOrderMtl::getMtlId, mtlId).eq(SaleOrderMtl::getRowSts, RowSts.EFFECTIVE.getId());
        SaleOrderMtl saleOrderMtl = saleOrderMtlService.getOne(lambdaQueryWrapper);
        if (null != saleOrderMtl) {
            Material material = materialService.getById(saleOrderMtl.getMtlId());
            if (null != material) {
                saleOrderMtl.setMtlCode(material.getCode());
                saleOrderMtl.setMtl(material.getName());
                saleOrderMtl.setSpecification(material.getSpecification());
            }
        }
        return Result.ok(saleOrderMtl);
    }
}
