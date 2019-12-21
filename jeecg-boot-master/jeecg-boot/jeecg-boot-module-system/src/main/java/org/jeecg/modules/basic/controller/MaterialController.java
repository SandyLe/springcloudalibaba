package org.jeecg.modules.basic.controller;

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
import org.jeecg.modules.basic.entity.MaterialBrand;
import org.jeecg.modules.basic.entity.MaterialType;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.service.MaterialBrandService;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialTypeService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
     * 添加
     *
     * @param material
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加产品")
    @ApiOperation(value = "添加产品", notes = "添加产品")
    public Result<?> add(@RequestBody Material material) {
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
        return Result.ok(material);
    }
}
