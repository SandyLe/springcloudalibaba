package org.jeecg.modules.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.LogisticsCompany;
import org.jeecg.modules.basic.service.LogisticsCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "物流公司")
@RestController
@RequestMapping("/logisticsCompany")
public class LogisticsCompanyController {

    @Autowired
    private LogisticsCompanyService logisticsCompanyService;

    /**
     * 分页列表查询
     *
     * @param logisticsCompany
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取物流公司数据列表", notes = "获取所有物流公司数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(LogisticsCompany logisticsCompany, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<LogisticsCompany> queryWrapper = QueryGenerator.initQueryWrapper(logisticsCompany, req.getParameterMap());
        Page<LogisticsCompany> page = new Page<LogisticsCompany>(pageNo, pageSize);
        IPage<LogisticsCompany> pageList = logisticsCompanyService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param logisticsCompany
     * @param req
     * @return
     */
    @ApiOperation(value = "获取物流公司数据", notes = "获取所有物流公司数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(LogisticsCompany logisticsCompany, HttpServletRequest req) {
        QueryWrapper<LogisticsCompany> queryWrapper = QueryGenerator.initQueryWrapper(logisticsCompany, req.getParameterMap());
        List<LogisticsCompany> list = logisticsCompanyService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param logisticsCompany
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加物流公司")
    @ApiOperation(value = "添加物流公司", notes = "添加物流公司")
    public Result<?> add(@RequestBody LogisticsCompany logisticsCompany) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(logisticsCompany.getCompanyId())) {
            logisticsCompany.setCompanyId(sysUser.getCompanyId());
        }
        logisticsCompanyService.save(logisticsCompany);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param logisticsCompany
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改物流公司")
    @ApiOperation(value = "修改物流公司", notes = "修改物流公司")
    public Result<?> edit(@RequestBody LogisticsCompany logisticsCompany){

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(logisticsCompany.getCompanyId())) {
            logisticsCompany.setCompanyId(sysUser.getCompanyId());
        }
        logisticsCompanyService.updateById(logisticsCompany);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除物流公司")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除物流公司", notes = "通过ID删除物流公司")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        logisticsCompanyService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除物流公司", notes = "批量删除物流公司")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.logisticsCompanyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询物流公司", notes = "通过ID查询物流公司")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        LogisticsCompany logisticsCompany = logisticsCompanyService.getById(id);
        return Result.ok(logisticsCompany);
    }
}
