package org.jeecg.modules.report.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.report.entity.Report;
import org.jeecg.modules.report.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/23 11:27
 * @Version: V1.0
 */

@Api(tags = "报表统计功能")
@RestController
@RequestMapping("/report")
public class ReportController extends JeecgController<Report, IReportService> {

    @Autowired
    private IReportService reportService;

    @ApiOperation(value = "获取报表列表", notes = "获取指定条件报表列表")
    @GetMapping("/getPageList")
    public Result<?> queryPageList(Report report,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Report> queryWrapper = QueryGenerator.initQueryWrapper(report, req.getParameterMap());
        IPage<Report> pageData = reportService.page(new Page<>(pageNo, pageSize), queryWrapper);

        return Result.ok(pageData);
    }

    @ApiOperation(value = "获取报表列表", notes = "获取指定条件报表列表")
    @GetMapping("/getList")
    public Result<?> queryList(Report report, HttpServletRequest req) {
        QueryWrapper<Report> queryWrapper = QueryGenerator.initQueryWrapper(report, req.getParameterMap());
        List<Report> data = reportService.list(queryWrapper);
        return Result.ok(data);
    }

    @ApiOperation(value = "添加报表", notes = "添加新报表")
    @PostMapping("/add")
    public Result<?> add(@RequestBody Report report) {
        reportService.save(report);
        return Result.ok();
    }


    @ApiOperation(value = "修改报表", notes = "修改报表数据")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody Report report) {
        reportService.updateById(report);
        return Result.ok();
    }


    @ApiOperation(value = "通过ID删除报表", notes = "通过ID删除报表")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        reportService.removeById(id);
        return Result.ok();
    }


    @ApiOperation(value = "批量删除统计报表", notes = "批量删除统计报表")
    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        reportService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }


    @ApiOperation(value = "通过ID查询报表", notes = "通过ID查询报表")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Report report = reportService.getById(id);
        if (report == null) {
            return Result.ok("未找到对应数据");
        }

        return Result.ok(report);
    }


    /*@RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Report repairOrder) {
        return super.exportXls(request, repairOrder, Report.class, "统计报表");
    }


    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Report.class);
    }*/

}
