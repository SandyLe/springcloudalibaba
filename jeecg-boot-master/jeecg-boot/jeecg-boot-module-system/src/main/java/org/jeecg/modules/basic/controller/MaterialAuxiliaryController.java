package org.jeecg.modules.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.basic.entity.MaterialAuxiliary;
import org.jeecg.modules.basic.service.MaterialAuxiliaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "产品辅助属性")
@RestController
@RequestMapping("/materialAuxiliary")
public class MaterialAuxiliaryController {

    @Autowired
    private MaterialAuxiliaryService materialAuxiliaryService;

    /**
     * 分页列表查询
     *
     * @param materialAuxiliary
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取辅助属性数据列表", notes = "获取所有辅助属性数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(MaterialAuxiliary materialAuxiliary, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<MaterialAuxiliary> queryWrapper = QueryGenerator.initQueryWrapper(materialAuxiliary, req.getParameterMap());
        Page<MaterialAuxiliary> page = new Page<MaterialAuxiliary>(pageNo, pageSize);
        IPage<MaterialAuxiliary> pageList = materialAuxiliaryService.page(page, queryWrapper);
        List<MaterialAuxiliary> data = pageList.getRecords();
        pageList.setRecords(data);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param materialAuxiliary
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品辅助属性数据", notes = "获取所有产品辅助属性数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(MaterialAuxiliary materialAuxiliary, HttpServletRequest req) {
        QueryWrapper<MaterialAuxiliary> queryWrapper = QueryGenerator.initQueryWrapper(materialAuxiliary, req.getParameterMap());
        List<MaterialAuxiliary> list = materialAuxiliaryService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param materialAuxiliary
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加辅助属性")
    @ApiOperation(value = "添加辅助属性", notes = "添加辅助属性")
    public Result<?> add(@RequestBody MaterialAuxiliary materialAuxiliary) {
        materialAuxiliaryService.save(materialAuxiliary);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param materialAuxiliary
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改辅助属性")
    @ApiOperation(value = "修改辅助属性", notes = "修改辅助属性")
    public Result<?> edit(@RequestBody MaterialAuxiliary materialAuxiliary){
        materialAuxiliaryService.updateById(materialAuxiliary);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除辅助属性")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除辅助属性", notes = "通过ID删除辅助属性")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        materialAuxiliaryService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除辅助属性", notes = "批量删除辅助属性")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.materialAuxiliaryService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询辅助属性", notes = "通过ID查询辅助属性")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        MaterialAuxiliary materialAuxiliary = materialAuxiliaryService.getById(id);
        return Result.ok(materialAuxiliary);
    }

    /**
     * 根据SourceIds获取
     *
     * @param sourceIds
     * @return
     */
    @ApiOperation(value = "根据IDS获取", notes = "根据IDS获取")
    @GetMapping(value = "/getListBySourceIds")
    public Result<?> getListBySourceIds(@RequestParam(name = "sourceIds") List<String> sourceIds) {
        LambdaQueryWrapper<MaterialAuxiliary> queryWrapper = new LambdaQueryWrapper<MaterialAuxiliary>();
        queryWrapper.in(MaterialAuxiliary::getSourceId, sourceIds);
        List<MaterialAuxiliary> list = materialAuxiliaryService.list(queryWrapper);
        return Result.ok(list);
    }
}
