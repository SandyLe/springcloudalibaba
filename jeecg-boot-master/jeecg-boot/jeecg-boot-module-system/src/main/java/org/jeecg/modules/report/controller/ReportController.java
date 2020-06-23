package org.jeecg.modules.report.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.common.enums.FunctionType;
import org.jeecg.common.enums.PageType;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.report.entity.Report;
import org.jeecg.modules.report.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/23 11:27
 * @Version: V1.0
 */

@RestController("/report")
public class ReportController extends JeecgController<Report, IReportService> {

    @Autowired
    private IReportService reportService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(Report report,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Report> queryWrapper = QueryGenerator.initQueryWrapper(report, req.getParameterMap());

        IPage<Report> pageData = reportService.page(new Page<>(pageNo, pageSize), queryWrapper);

        pageData.getRecords().stream().forEach(p->{
            p.setFunctionTypeIdName(EnumConvertUtils.getName(FunctionType.class, p.getFunctionTypeId()));
            p.setPageTypeName(EnumConvertUtils.getName(PageType.class, p.getPageType()));
        });

        return Result.ok(pageData.getRecords());
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Report report) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(report.getCompanyId())) {
            report.setCompanyId(sysUser.getCompanyId());
        }
        reportService.save(report);
        return Result.ok();
    }


    @PostMapping("/edit")
    public Result<?> edit(@RequestBody Report report) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(report.getCompanyId())) {
            report.setCompanyId(sysUser.getCompanyId());
        }
        reportService.save(report);
        return Result.ok();
    }


    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) Integer id) {
        reportService.removeById(id);
        return Result.ok();
    }


    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        reportService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }


    @GetMapping("/getOne")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Report report = reportService.getById(id);
        if (report == null) {
            return Result.ok("未找到对应数据");
        }

        report.setFunctionTypeIdName(EnumConvertUtils.getName(FunctionType.class, report.getFunctionTypeId()));
        report.setPageTypeName(EnumConvertUtils.getName(PageType.class, report.getPageType()));

        return Result.ok(report);
    }


    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Report repairOrder) {
        return super.exportXls(request, repairOrder, Report.class, "拆卸单列表");
    }


    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Report.class);
    }

}
