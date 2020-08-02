package org.jeecg.modules.saleorder.controller;

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
import org.jeecg.common.enums.ReturnType;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.common.enums.DiscountType;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnMtl;
import org.jeecg.modules.saleorder.service.SaleOrderReturnMtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "销售退货产品明细")
@RestController
@RequestMapping("/saleOrderReturnMtl")
public class SaleOrderReturnMtlController {

    @Autowired
    private SaleOrderReturnMtlService saleOrderReturnMtlService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialUnitService materialUnitService;
    /**
     * 添加
     *
     * @param saleOrderReturnMtl
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加销售退货产品")
    @ApiOperation(value = "添加销售退货产品", notes = "添加销售退货产品")
    public Result<?> add(@RequestBody SaleOrderReturnMtl saleOrderReturnMtl) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(saleOrderReturnMtl.getCompanyId())) {
            saleOrderReturnMtl.setCompanyId(sysUser.getCompanyId());
        }
        if (null == saleOrderReturnMtl.getRowSts()) {
            saleOrderReturnMtl.setRowSts(RowSts.EFFECTIVE.getId());
        }
        saleOrderReturnMtlService.saveSaleOrderReturnMtl(saleOrderReturnMtl);
        return Result.ok("添加成功！");
    }
    /**
     * 获取所有数据
     *
     * @param saleOrderReturnMtl
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售退货产品数据", notes = "获取所有销售退货产品数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrderReturnMtl saleOrderReturnMtl, HttpServletRequest req) {
        QueryWrapper<SaleOrderReturnMtl> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderReturnMtl, req.getParameterMap());
        List<SaleOrderReturnMtl> list = saleOrderReturnMtlService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param saleOrderReturnMtl
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售退货产品数据列表", notes = "获取所有销售退货产品数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(SaleOrderReturnMtl saleOrderReturnMtl, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {

        QueryWrapper<SaleOrderReturnMtl> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderReturnMtl, req.getParameterMap());
        Page<SaleOrderReturnMtl> page = new Page<SaleOrderReturnMtl>(pageNo, pageSize);

        IPage<SaleOrderReturnMtl> pageList = saleOrderReturnMtlService.page(page, queryWrapper);
        List<SaleOrderReturnMtl> saleOrderList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(saleOrderList)) {
            List<String> mtlIds = saleOrderList.stream().map(SaleOrderReturnMtl::getMtlId).collect(Collectors.toList());
            List<String> unitIds = saleOrderList.stream().map(SaleOrderReturnMtl::getUnitId).collect(Collectors.toList());
            Collection<Material> materials = materialService.listByIds(mtlIds);
            Collection<MaterialUnit> warehouses = materialUnitService.listByIds(unitIds);
            Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
            Map<String, String> mtlCodeMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getCode));
            Map<String, String> mtlSpecMap = materials.stream().filter(o-> StringUtils.isNotBlank(o.getSpecification())).collect(Collectors.toMap(Material::getId, Material::getSpecification));
            Map<String, String> unitMap = warehouses.stream().collect(Collectors.toMap(MaterialUnit:: getId, MaterialUnit:: getName));
            saleOrderList.stream().forEach(o->{
                o.setUnit(unitMap.get(o.getUnitId()));
                o.setMtl(mtlMap.get(o.getMtlId()));
                o.setMtlCode(mtlCodeMap.get(o.getMtlId()));
                o.setSpecification(mtlSpecMap.get(o.getMtlId()));
                o.setReturnTypeName(ReturnType.getName(o.getReturnTypeId()));
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
     * @param saleOrderReturnMtl
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改销售退货产品")
    @ApiOperation(value = "修改销售退货产品", notes = "修改销售退货产品")
    public Result<?> edit(@RequestBody SaleOrderReturnMtl saleOrderReturnMtl){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(saleOrderReturnMtl.getCompanyId())) {
            saleOrderReturnMtl.setCompanyId(sysUser.getCompanyId());
        }
        saleOrderReturnMtlService.saveSaleOrderReturnMtl(saleOrderReturnMtl);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除销售退货产品")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除销售退货产品", notes = "通过ID删除销售退货产品")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        saleOrderReturnMtlService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除销售退货产品", notes = "批量删除销售退货产品")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.saleOrderReturnMtlService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询销售退货产品", notes = "通过ID查询销售退货产品")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        SaleOrderReturnMtl saleOrderReturnMtl = saleOrderReturnMtlService.getById(id);
        return Result.ok(saleOrderReturnMtl);
    }
}
