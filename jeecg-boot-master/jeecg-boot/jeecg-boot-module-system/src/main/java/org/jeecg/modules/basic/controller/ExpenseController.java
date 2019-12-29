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
import org.jeecg.modules.basic.entity.Expense;
import org.jeecg.modules.basic.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "费用类型")
@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    /**
     * 分页列表查询
     *
     * @param expense
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取费用类型数据列表", notes = "获取所有费用类型数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(Expense expense, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Expense> queryWrapper = QueryGenerator.initQueryWrapper(expense, req.getParameterMap());
        Page<Expense> page = new Page<Expense>(pageNo, pageSize);
        IPage<Expense> pageList = expenseService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param expense
     * @param req
     * @return
     */
    @ApiOperation(value = "获取费用类型数据", notes = "获取所有费用类型数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(Expense expense, HttpServletRequest req) {
        QueryWrapper<Expense> queryWrapper = QueryGenerator.initQueryWrapper(expense, req.getParameterMap());
        List<Expense> list = expenseService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param expense
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加费用类型")
    @ApiOperation(value = "添加费用类型", notes = "添加费用类型")
    public Result<?> add(@RequestBody Expense expense) {
        expenseService.save(expense);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param expense
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改费用类型")
    @ApiOperation(value = "修改费用类型", notes = "修改费用类型")
    public Result<?> edit(@RequestBody Expense expense){
        expenseService.updateById(expense);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除费用类型")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除费用类型", notes = "通过ID删除费用类型")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        expenseService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除费用类型", notes = "批量删除费用类型")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.expenseService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询费用类型", notes = "通过ID查询费用类型")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        Expense expense = expenseService.getById(id);
        return Result.ok(expense);
    }
}
