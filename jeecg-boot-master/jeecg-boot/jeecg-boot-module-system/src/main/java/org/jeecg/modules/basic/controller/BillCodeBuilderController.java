package org.jeecg.modules.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.entity.BillCodeBuilder;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "单号生成规则")
@RestController
@RequestMapping("/billCodeBuilder")
public class BillCodeBuilderController {

    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    /**
     * 分页列表查询
     *
     * @param billCodeBuilder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取地区数据列表", notes = "获取所有地区数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(BillCodeBuilder billCodeBuilder, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<BillCodeBuilder> queryWrapper = QueryGenerator.initQueryWrapper(billCodeBuilder, req.getParameterMap());
        Page<BillCodeBuilder> page = new Page<BillCodeBuilder>(pageNo, pageSize);

        IPage<BillCodeBuilder> pageList = billCodeBuilderService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param billCodeBuilder
     * @param req
     * @return
     */
    @ApiOperation(value = "获取客户来源数据", notes = "获取所有客户来源数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(BillCodeBuilder billCodeBuilder, HttpServletRequest req) {
        QueryWrapper<BillCodeBuilder> queryWrapper = QueryGenerator.initQueryWrapper(billCodeBuilder, req.getParameterMap());
        List<BillCodeBuilder> list = billCodeBuilderService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param billCodeBuilder
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加地区")
    @ApiOperation(value = "添加地区", notes = "添加地区")
    public Result<?> add(@RequestBody BillCodeBuilder billCodeBuilder) {
        if (StringUtils.isBlank(billCodeBuilder.getCompanyId())) {
            billCodeBuilder.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        billCodeBuilderService.save(billCodeBuilder);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param billCodeBuilder
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改地区")
    @ApiOperation(value = "修改地区", notes = "修改地区")
    public Result<?> edit(@RequestBody BillCodeBuilder billCodeBuilder){
        if (StringUtils.isBlank(billCodeBuilder.getCompanyId())) {
            billCodeBuilder.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        billCodeBuilderService.updateById(billCodeBuilder);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除地区")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除地区", notes = "通过ID删除地区")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        billCodeBuilderService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除地区", notes = "批量删除地区")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.billCodeBuilderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询地区", notes = "通过ID查询地区")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        BillCodeBuilder BillCodeBuilder = billCodeBuilderService.getById(id);
        return Result.ok(BillCodeBuilder);
    }
}
