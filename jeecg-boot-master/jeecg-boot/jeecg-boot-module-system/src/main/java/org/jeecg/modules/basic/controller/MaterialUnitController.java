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
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Api(tags = "产品单位")
@RestController
@RequestMapping("/materialUnit")
public class MaterialUnitController {

    @Autowired
    private MaterialUnitService materialUnitService;

    /**
     * 分页列表查询
     *
     * @param materialUnit
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品单位数据列表", notes = "获取所有产品单位数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(MaterialUnit materialUnit, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<MaterialUnit> queryWrapper = QueryGenerator.initQueryWrapper(materialUnit, req.getParameterMap());
        Page<MaterialUnit> page = new Page<MaterialUnit>(pageNo, pageSize);

        IPage<MaterialUnit> pageList = materialUnitService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }
    /**
     * 添加
     *
     * @param materialUnit
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加产品单位")
    @ApiOperation(value = "添加产品单位", notes = "添加产品单位")
    public Result<?> add(@RequestBody MaterialUnit materialUnit) {
        materialUnitService.save(materialUnit);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param materialUnit
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改产品单位")
    @ApiOperation(value = "修改产品单位", notes = "修改产品单位")
    public Result<?> edit(@RequestBody MaterialUnit materialUnit){
        materialUnitService.updateById(materialUnit);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除产品单位")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除产品单位", notes = "通过ID删除产品单位")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        materialUnitService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除产品单位", notes = "批量删除产品单位")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.materialUnitService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询产品单位", notes = "通过ID查询产品单位")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        MaterialUnit materialUnit = materialUnitService.getById(id);
        return Result.ok(materialUnit);
    }
}
