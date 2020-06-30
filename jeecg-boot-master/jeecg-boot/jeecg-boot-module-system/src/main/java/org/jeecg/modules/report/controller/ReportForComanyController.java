package org.jeecg.modules.report.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.report.entity.*;
import org.jeecg.modules.report.service.IReportForCompanyService;
import org.jeecg.modules.report.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/23 11:27
 * @Version: V1.0
 */

@Api(tags = "报表统计功能")
@RestController
@RequestMapping("/report/company")
public class ReportForComanyController extends JeecgController<ReportForCompany, IReportForCompanyService> {

    @Autowired
    private IReportForCompanyService reportForCompanyService;
    @Autowired
    private IReportService reportService;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @ApiOperation(value = "获取公司报表列表", notes = "获取指定条件公司报表列表")
    @GetMapping("/getList")
    public Result<?> queryList(ReportForCompany reportForCompany, HttpServletRequest req) {
        QueryWrapper<ReportForCompany> queryWrapper = QueryGenerator.initQueryWrapper(reportForCompany, req.getParameterMap());
        List<ReportForCompany> data = reportForCompanyService.list(queryWrapper);

        return Result.ok(data);
    }

    @ApiOperation(value = "添加公司报表", notes = "添加新公司报表")
    @PostMapping("/multiadd")
    public Result<?> add(@RequestBody ReportForCompanyInputBean model) {
        if (StringUtils.isNotEmpty(model.getCompanyId()) && ArrayUtil.isNotEmpty(model.getReportids())){
            // 1、首先移除历史数据
            LambdaQueryWrapper<ReportForCompany> lambdaQueryWrapper = new LambdaQueryWrapper<ReportForCompany>().eq(ReportForCompany::getCompanyId, model.getCompanyId());
            List<ReportForCompany> data = reportForCompanyService.list(lambdaQueryWrapper);
            if (CollUtil.isNotEmpty(data)){
                reportForCompanyService.removeByIds(data.stream().map(ReportForCompany::getId).collect(Collectors.toList()));
            }

            // 2、然后再插入新数据
            List<ReportForCompany> list = new ArrayList<>();
            Arrays.stream(model.getReportids()).forEach(m->{
                ReportForCompany reportForCompany = new ReportForCompany();
                reportForCompany.setCompanyId(model.getCompanyId());
                reportForCompany.setReportId(m);
                list.add(reportForCompany);
            });
            reportForCompanyService.saveBatch(list);
        }
        return Result.ok();
    }

    @ApiOperation(value = "通过公司ID查询公司报表", notes = "通过公司ID查询公司报表")
    @GetMapping("/queryByCompanyId")
    public Result<?> queryByCompanyId(@RequestParam(name = "companyId", required = true) String companyId, @RequestParam(name = "pageType", required = false) String pageType) {
        LambdaQueryWrapper<ReportForCompany> lambdaQueryWrapper = new LambdaQueryWrapper<ReportForCompany>().eq(ReportForCompany::getCompanyId, companyId);
        List<ReportForCompany> data = reportForCompanyService.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(data)) {
            return Result.ok("未找到对应数据");
        }
        LambdaQueryWrapper<Report> reportLambdaQueryWrapper = new LambdaQueryWrapper<Report>().in(Report::getId, data.stream().map(ReportForCompany::getReportId).collect(Collectors.toList()));
        if (StringUtils.isNotEmpty(pageType)){
            reportLambdaQueryWrapper.eq(Report::getPageType, pageType);
        }

        List<Report> allReport = reportService.list(reportLambdaQueryWrapper);
        List<ShowTypeLabelOutputBean> labelOutputBeans = new ArrayList<>();
        List<ShowTypeRankingListOutputBean> rankingListOutputBeans = new ArrayList<>();

        String companyTag = "#companyid#";
        for (Report report:allReport){
            //  文本结构统计结果
            if (StringUtils.equalsIgnoreCase("label", report.getShowType())){
                ShowTypeLabelOutputBean label = new ShowTypeLabelOutputBean();
                label.setTitle(report.getName());
                if (report.getQuerySql().contains(companyTag)){
                    label.setValue(jdbcTemplate.queryForObject(StringUtils.replace(report.getQuerySql(), companyTag, companyId), String.class));
                }
                else{
                    label.setValue(jdbcTemplate.queryForObject(report.getQuerySql(), String.class));
                }
                labelOutputBeans.add(label);
            }

            //  排行榜结构统计结果
            else if (StringUtils.equalsIgnoreCase(companyTag, report.getShowType())){
                ShowTypeRankingListOutputBean rankingList = new ShowTypeRankingListOutputBean();
                rankingList.setTitle(report.getName());
                if (report.getQuerySql().contains(companyTag)){
                    rankingList.setData(jdbcTemplate.queryForObject(StringUtils.replace(report.getQuerySql(), companyTag, companyId), Object.class));
                }
                else{
                    rankingList.setData(jdbcTemplate.queryForObject(report.getQuerySql(), Object.class));
                }
                rankingListOutputBeans.add(rankingList);
            }
        }

        PageReportOutputBean pageReportOutputBean = new PageReportOutputBean();
        pageReportOutputBean.setShowTypeLabelOutputBeans(labelOutputBeans);
        pageReportOutputBean.setShowTypeRankingListOutputBeans(rankingListOutputBeans);

        return Result.ok(pageReportOutputBean);
    }

    /*@ApiOperation(value = "获取公司报表列表", notes = "获取指定条件公司报表列表")
    @GetMapping("/getPageList")
    public Result<?> queryPageList(ReportForCompany reportForCompany,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ReportForCompany> queryWrapper = QueryGenerator.initQueryWrapper(reportForCompany, req.getParameterMap());
        IPage<ReportForCompany> pageData = reportForCompanyService.page(new Page<>(pageNo, pageSize), queryWrapper);

        return Result.ok(pageData);
    }*/

    /*@ApiOperation(value = "添加公司报表", notes = "添加新公司报表")
    @PostMapping("/add")
    public Result<?> add(@RequestBody ReportForCompany report) {
        reportForCompanyService.save(report);
        return Result.ok();
    }
*/

    /*@ApiOperation(value = "修改公司报表", notes = "修改公司报表数据")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody ReportForCompany report) {
        reportForCompanyService.updateById(report);
        return Result.ok();
    }*/

    /*@ApiOperation(value = "通过ID删除公司报表", notes = "通过ID删除公司报表")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        reportForCompanyService.removeById(id);
        return Result.ok();
    }*/

    /*@ApiOperation(value = "批量删除公司统计报表", notes = "批量删除公司统计报表")
    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        reportForCompanyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }*/


}
