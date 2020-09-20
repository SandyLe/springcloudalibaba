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
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.entity.Supplementary;
import org.jeecg.modules.basic.service.SupplementaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "辅助属性")
@RestController
@RequestMapping("/supplementary")
public class SupplementaryController {

    @Autowired
    private SupplementaryService supplementaryService;

    /**
     * 分页列表查询
     *
     * @param supplementary
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取辅助属性数据列表", notes = "获取所有辅助属性数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(Supplementary supplementary, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Supplementary> queryWrapper = QueryGenerator.initQueryWrapper(supplementary, req.getParameterMap());
        Page<Supplementary> page = new Page<Supplementary>(pageNo, pageSize);
        IPage<Supplementary> pageList = supplementaryService.page(page, queryWrapper);
        List<Supplementary> data = pageList.getRecords();
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
     * @param supplementary
     * @param req
     * @return
     */
    @ApiOperation(value = "获取辅助属性数据", notes = "获取所有辅助属性数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(Supplementary supplementary, HttpServletRequest req) {
        QueryWrapper<Supplementary> queryWrapper = QueryGenerator.initQueryWrapper(supplementary, req.getParameterMap());
        List<Supplementary> list = supplementaryService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param supplementary
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加辅助属性")
    @ApiOperation(value = "添加辅助属性", notes = "添加辅助属性")
    public Result<?> add(@RequestBody Supplementary supplementary) {

        supplementary.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        supplementaryService.save(supplementary);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param supplementary
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改辅助属性")
    @ApiOperation(value = "修改辅助属性", notes = "修改辅助属性")
    public Result<?> edit(@RequestBody Supplementary supplementary){
        supplementary.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        supplementaryService.updateById(supplementary);
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
        supplementaryService.removeById(id);
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
        this.supplementaryService.removeByIds(Arrays.asList(ids.split(",")));
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
        Supplementary supplementary = supplementaryService.getById(id);
        return Result.ok(supplementary);
    }

}
