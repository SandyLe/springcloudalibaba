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
import org.jeecg.modules.basic.entity.PurchaseBatch;
import org.jeecg.modules.basic.service.PurchaseBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "采购批次")
@RestController
@RequestMapping("/purchaseBatch")
public class PurchaseBatchController {

    @Autowired
    private PurchaseBatchService purchaseBatchService;

    /**
     * 分页列表查询
     *
     * @param purchaseBatch
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取采购批次数据列表", notes = "获取所有采购批次数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(PurchaseBatch purchaseBatch, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<PurchaseBatch> queryWrapper = QueryGenerator.initQueryWrapper(purchaseBatch, req.getParameterMap());
        Page<PurchaseBatch> page = new Page<PurchaseBatch>(pageNo, pageSize);
        IPage<PurchaseBatch> pageList = purchaseBatchService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param purchaseBatch
     * @param req
     * @return
     */
    @ApiOperation(value = "获取采购批次数据", notes = "获取所有采购批次数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(PurchaseBatch purchaseBatch, HttpServletRequest req) {
        QueryWrapper<PurchaseBatch> queryWrapper = QueryGenerator.initQueryWrapper(purchaseBatch, req.getParameterMap());
        List<PurchaseBatch> list = purchaseBatchService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param purchaseBatch
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加采购批次")
    @ApiOperation(value = "添加采购批次", notes = "添加采购批次")
    public Result<?> add(@RequestBody PurchaseBatch purchaseBatch) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(purchaseBatch.getCompanyId())) {
            purchaseBatch.setCompanyId(sysUser.getCompanyId());
        }
        purchaseBatchService.save(purchaseBatch);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param purchaseBatch
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改采购批次")
    @ApiOperation(value = "修改采购批次", notes = "修改采购批次")
    public Result<?> edit(@RequestBody PurchaseBatch purchaseBatch){

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(purchaseBatch.getCompanyId())) {
            purchaseBatch.setCompanyId(sysUser.getCompanyId());
        }
        purchaseBatchService.updateById(purchaseBatch);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除采购批次")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除采购批次", notes = "通过ID删除采购批次")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        purchaseBatchService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除采购批次", notes = "批量删除采购批次")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.purchaseBatchService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询采购批次", notes = "通过ID查询采购批次")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        PurchaseBatch purchaseBatch = purchaseBatchService.getById(id);
        return Result.ok(purchaseBatch);
    }
}
