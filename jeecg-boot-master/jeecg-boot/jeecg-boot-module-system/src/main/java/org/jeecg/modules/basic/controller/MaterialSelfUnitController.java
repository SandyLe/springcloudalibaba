package org.jeecg.modules.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
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
import org.jeecg.modules.basic.entity.MaterialSelfUnit;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.service.MaterialSelfUnitService;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "产品自身单位")
@RestController
@RequestMapping("/materialSelfUnit")
public class MaterialSelfUnitController {

    @Autowired
    private MaterialSelfUnitService materialSelfUnitService;

    @Autowired
    private MaterialUnitService materialUnitService;

    @Autowired
    private MaterialService materialService;

    /**
     * 分页列表查询
     *
     * @param materialSelfUnit
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品自身单位数据列表", notes = "获取所有产品自身单位数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(MaterialSelfUnit materialSelfUnit, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<MaterialSelfUnit> queryWrapper = QueryGenerator.initQueryWrapper(materialSelfUnit, req.getParameterMap());
        Page<MaterialSelfUnit> page = new Page<MaterialSelfUnit>(pageNo, pageSize);
        IPage<MaterialSelfUnit> pageList = materialSelfUnitService.page(page, queryWrapper);
        List<MaterialSelfUnit> records = page.getRecords();
        List<String> unitIds = records.stream().map(MaterialSelfUnit::getUnitId).collect(Collectors.toList());
        Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
        Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
        records.stream().forEach(o->{
            o.setUnit(unitMap.get(o.getUnitId()));
        });
        pageList.setRecords(records);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param materialSelfUnit
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品自身单位数据", notes = "获取所有产品自身单位数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(MaterialSelfUnit materialSelfUnit, HttpServletRequest req) {
        QueryWrapper<MaterialSelfUnit> queryWrapper = QueryGenerator.initQueryWrapper(materialSelfUnit, req.getParameterMap());
        List<MaterialSelfUnit> list = materialSelfUnitService.list(queryWrapper);
        List<String> unitIds = list.stream().map(MaterialSelfUnit::getUnitId).collect(Collectors.toList());
        Collection<MaterialUnit> units = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(unitIds)) {
            units = materialUnitService.listByIds(unitIds);
            Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
            list.stream().forEach(o->{
                o.setUnit(unitMap.get(o.getUnitId()));
            });
        }
        String addSelf = req.getParameter("addSelf").toLowerCase();
        String sourceId = req.getParameter("sourceId");
        if(StringUtils.isNotBlank(addSelf) && "true".equals(addSelf) && StringUtils.isNotBlank(sourceId)){
            Material material = materialService.getById(sourceId);
            if (StringUtils.isNotBlank(material.getUnitId()) && !unitIds.contains(material.getUnitId())) {
                MaterialSelfUnit msu = new MaterialSelfUnit();
                msu.setUnitId(material.getUnitId());
                MaterialUnit materialUnit = materialUnitService.getById(material.getUnitId());
                msu.setUnit(materialUnit.getName());
                msu.setQty(BigDecimal.ONE);
                msu.setSourceId(sourceId);
                list.add(msu);
            }

        }
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param materialSelfUnit
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加产品自身单位")
    @ApiOperation(value = "添加产品自身单位", notes = "添加产品自身单位")
    public Result<?> add(@RequestBody MaterialSelfUnit materialSelfUnit) {
        materialSelfUnitService.save(materialSelfUnit);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param materialSelfUnit
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改产品自身单位")
    @ApiOperation(value = "修改产品自身单位", notes = "修改产品自身单位")
    public Result<?> edit(@RequestBody MaterialSelfUnit materialSelfUnit){
        materialSelfUnitService.updateById(materialSelfUnit);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除产品自身单位")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除产品自身单位", notes = "通过ID删除产品自身单位")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        materialSelfUnitService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除产品自身单位", notes = "批量删除产品自身单位")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.materialSelfUnitService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询产品自身单位", notes = "通过ID查询产品自身单位")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        MaterialSelfUnit materialSelfUnit = materialSelfUnitService.getById(id);
        return Result.ok(materialSelfUnit);
    }
}
