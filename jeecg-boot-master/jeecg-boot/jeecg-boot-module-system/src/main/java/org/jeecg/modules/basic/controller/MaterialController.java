package org.jeecg.modules.basic.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.dto.MaterialExcelDto;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialBrand;
import org.jeecg.modules.basic.entity.MaterialType;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.common.enums.BillType;
import org.jeecg.modules.basic.service.*;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "产品")
@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialBrandService materialBrandService;
    @Autowired
    private MaterialTypeService materialTypeService;
    @Autowired
    private MaterialUnitService materialUnitService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    /**
     * 分页列表查询
     *
     * @param material
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品数据列表", notes = "获取所有产品数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(Material material, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Material> queryWrapper = QueryGenerator.initQueryWrapper(material, req.getParameterMap());
        Page<Material> page = new Page<Material>(pageNo, pageSize);
        IPage<Material> pageList = materialService.page(page, queryWrapper);
        List<Material> result = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(result)) {
            List<String> brandIds = result.stream().map(Material::getBrandId).collect(Collectors.toList());;
            List<String> typeIds = result.stream().map(Material::getTypeId).collect(Collectors.toList());
            List<String> unitIds = result.stream().map(Material::getUnitId).collect(Collectors.toList());
            Collection<MaterialBrand> brands = materialBrandService.listByIds(brandIds);
            Collection<MaterialType> types = materialTypeService.listByIds(typeIds);
            Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
            Map<String, String> brandIdNameMap = brands.stream().collect(Collectors.toMap(MaterialBrand::getId, MaterialBrand::getName));
            Map<String, String> typeIdNameMap = types.stream().collect(Collectors.toMap(MaterialType::getId, MaterialType::getName));
            Map<String, String> unitIdNameMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
            result.stream().forEach(o->{
                o.setBrand(brandIdNameMap.get(o.getBrandId()));
                o.setType(typeIdNameMap.get(o.getTypeId()));
                o.setUnit(unitIdNameMap.get(o.getUnitId()));
            });
        }
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param material
     * @param req
     * @return
     */
    @ApiOperation(value = "获取客户来源数据", notes = "获取所有客户来源数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(Material material, HttpServletRequest req) {
        QueryWrapper<Material> queryWrapper = QueryGenerator.initQueryWrapper(material, req.getParameterMap());
        List<Material> list = materialService.list(queryWrapper);
        return Result.ok(list);
    }

    /**
     * 根据IDS获取
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "根据IDS获取", notes = "根据IDS获取")
    @GetMapping(value = "/getListByIds")
    public Result<?> getListByIds(@RequestParam(name = "ids") List<String> ids) {
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<Material>();
        queryWrapper.in(Material::getId, ids);
        List<Material> list = materialService.list(queryWrapper);
        return Result.ok(list);
    }

    /**
     * 添加
     *
     * @param material
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加产品")
    @ApiOperation(value = "添加产品", notes = "添加产品")
    public Result<?> add(@RequestBody Material material) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(material.getCompanyId())) {
            material.setCompanyId(sysUser.getCompanyId());
        }
        if (StringUtils.isEmpty(material.getId())) {
            material.setCode(billCodeBuilderService.getBillCode(BillType.MATERIAL.getId()));
        }
        Material existCode = materialService.getOne(new LambdaQueryWrapper<Material>().eq(Material::getCode, material.getCode()).ne(Material::getId, material.getId()));
        Assert.isNull(existCode, "编号已存在！");
        materialService.save(material);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param material
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改产品")
    @ApiOperation(value = "修改产品", notes = "修改产品")
    public Result<?> edit(@RequestBody Material material){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(material.getCompanyId())) {
            material.setCompanyId(sysUser.getCompanyId());
        }
        Material existCode = materialService.getOne(new LambdaQueryWrapper<Material>().eq(Material::getCode, material.getCode()).ne(Material::getId, material.getId()));
        Assert.isNull(existCode, "编号已存在！");
        materialService.updateById(material);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除产品")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除产品", notes = "通过ID删除产品")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        materialService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除产品", notes = "批量删除产品")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.materialService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询产品", notes = "通过ID查询产品")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        Material material = materialService.getById(id);
        if (null != material) {
            if (StringUtils.isNotBlank(material.getTypeId())) {
                MaterialType materialType = materialTypeService.getById(material.getTypeId());
                material.setType(null != materialType ? materialType.getName() : null);
            }
            if (StringUtils.isNotBlank(material.getBrandId())) {
                MaterialBrand materialBrand = materialBrandService.getById(material.getBrandId());
                material.setBrand(null != materialBrand ? materialBrand.getName() : null);
            }
            if (StringUtils.isNotBlank(material.getUnitId())) {
                MaterialUnit materialUnit = materialUnitService.getById(material.getUnitId());
                material.setUnit(null != materialUnit ? materialUnit.getName() : null);
            }
        }
        return Result.ok(material);
    }

    /**
     * 搜索产品
     *
     * @param keyword
     * @return
     */
    @ApiOperation(value = "搜索", notes = "搜索")
    @GetMapping(value = "/search")
    public Result<?> search(@ApiParam(name = "keyword", value = "关键词", required = true) @RequestParam(name = "keyword", required = false) String keyword) {
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<Material>();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like(Material::getName, keyword).or().like(Material::getCode, keyword).or().like(Material::getSpecification, keyword);
        }
        queryWrapper.orderByDesc(Material::getCreateTime).last("limit 0,20");
        List<Material> list = materialService.list(queryWrapper);
        return Result.ok(list);
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
//    @RequiresPermissions("user:import")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @Transactional
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        Collection<MaterialBrand> brands = materialBrandService.list();
        Collection<MaterialType> types = materialTypeService.list();
        Collection<MaterialUnit> units = materialUnitService.list();

        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<MaterialExcelDto> materialList = ExcelImportUtil.importExcel(file.getInputStream(), MaterialExcelDto.class, params);
                if (CollectionUtils.isNotEmpty(materialList)){

                    if (materialList.stream().anyMatch(p->StringUtils.isEmpty(p.getBrand()))){
                        return Result.error(String.format("抱歉！“品牌”列不能为空"));
                    }
                    Collection<String> brandsSubtract  = CollectionUtils.removeAll(materialList.stream().map(p->p.getBrand()).collect(Collectors.toList()),
                            brands.stream().map(p->p.getName()).collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(brandsSubtract)){
                        return Result.error(String.format("抱歉！请先新增以下品牌: %s", StringUtils.join(brandsSubtract.toArray(),",")));
                    }


                    if (materialList.stream().anyMatch(p->StringUtils.isEmpty(p.getType()))){
                        return Result.error(String.format("抱歉！“类型”列不能为空"));
                    }
                    Collection<String> typesSubtract = CollectionUtils.removeAll(materialList.stream().map(p->p.getType()).collect(Collectors.toList()),
                            types.stream().map(p->p.getName()).collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(typesSubtract)){
                        return Result.error(String.format("抱歉！请先新增以下类型: %s", StringUtils.join(typesSubtract.toArray(),",")));
                    }


                    if (materialList.stream().anyMatch(p->StringUtils.isEmpty(p.getUnit()))){
                        return Result.error(String.format("抱歉！“单位”列不能为空"));
                    }
                    Collection<String> unitsSubtract = CollectionUtils.removeAll(materialList.stream().map(p->p.getUnit()).collect(Collectors.toList()),
                            units.stream().map(p->p.getName()).collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(typesSubtract)){
                        return Result.error(String.format("抱歉！请先新增以下单位: %s", StringUtils.join(unitsSubtract.toArray(),",")));
                    }


                    List<Material> excelMaterial = new ArrayList<>();
                    Material material;
                    for (MaterialExcelDto materialExcelDto : materialList) {
                        System.out.println(JSON.toJSONString(materialExcelDto));
                        material = new Material();
                        material.setName(materialExcelDto.getName());
                        material.setCode(billCodeBuilderService.getBillCode(BillType.MATERIAL.getId()));
                        material.setSpecification(materialExcelDto.getSpecification());
                        material.setBrandId(brands.stream().filter(p-> ObjectUtils.equals(p.getName(), materialExcelDto.getBrand()) ).findFirst().get().getId());
                        material.setUnitId(units.stream().filter(p-> ObjectUtils.equals(p.getName(), materialExcelDto.getUnit()) ).findFirst().get().getId());
                        material.setTypeId(types.stream().filter(p-> ObjectUtils.equals(p.getName(), materialExcelDto.getType()) ).findFirst().get().getId());
                        excelMaterial.add(material);
                    }
                    materialService.saveBatch(excelMaterial);
                    return Result.ok("文件导入成功！数据行数：" + materialList.size());
                }
                return Result.error("抱歉! Excel中没有任何数据");

            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage(),e);
                return Result.error("抱歉! 发生错误:"+ e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return Result.error("文件导入失败！");
    }
}
