package org.jeecg.modules.saleorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Expense;
import org.jeecg.modules.basic.service.ExpenseService;
import org.jeecg.modules.saleorder.entity.SaleOrderExpense;
import org.jeecg.modules.saleorder.service.SaleOrderExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "单表销售订单")
@RestController
@RequestMapping("/saleOrderExpense")
public class SaleOrderExpenseController {

    @Autowired
    private SaleOrderExpenseService saleOrderExpenseService;
    @Autowired
    private ExpenseService expenseService;
    /**
     * 添加
     *
     * @param saleOrderExpense
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加销售订单")
    @ApiOperation(value = "添加销售订单", notes = "添加销售订单")
    public Result<?> add(@RequestBody SaleOrderExpense saleOrderExpense) {
        BigDecimal totalAmout = saleOrderExpenseService.saveSaleOrderExpense(saleOrderExpense);
        return Result.ok(totalAmout);
    }
    /**
     * 获取所有数据
     *
     * @param saleOrderExpense
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单数据", notes = "获取所有销售订单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrderExpense saleOrderExpense, HttpServletRequest req) {
        QueryWrapper<SaleOrderExpense> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderExpense, req.getParameterMap());
        List<SaleOrderExpense> saleOrderExpenses = saleOrderExpenseService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(saleOrderExpenses)) {
            List<String> expenseIds = saleOrderExpenses.stream().map(SaleOrderExpense::getExpenseId).collect(Collectors.toList());
            Collection<Expense> expenses = expenseService.listByIds(expenseIds);
            Map<String, String> expenseMap = expenses.stream().collect(Collectors.toMap(Expense::getId, Expense::getName));
            saleOrderExpenses.stream().forEach(o->{
                o.setExpense(expenseMap.get(o.getExpenseId()));
            });
        }
        return Result.ok(saleOrderExpenses);
    }
    /**
     * 分页列表查询
     *
     * @param saleOrderExpense
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单数据列表", notes = "获取所有销售订单数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(SaleOrderExpense saleOrderExpense, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SaleOrderExpense> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderExpense, req.getParameterMap());
        Page<SaleOrderExpense> page = new Page<SaleOrderExpense>(pageNo, pageSize);
        IPage<SaleOrderExpense> pageList = saleOrderExpenseService.page(page, queryWrapper);
        List<SaleOrderExpense> saleOrderList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(saleOrderList)) {
            List<String> expenseIds = saleOrderList.stream().map(SaleOrderExpense::getExpenseId).collect(Collectors.toList());
            Collection<Expense> expenses = expenseService.listByIds(expenseIds);
            Map<String, String> expenseMap = expenses.stream().collect(Collectors.toMap(Expense::getId, Expense::getName));
            saleOrderList.stream().forEach(o->{
                o.setExpense(expenseMap.get(o.getExpenseId()));
            });
        }

        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 修改
     *
     * @param saleOrderExpense
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改销售订单")
    @ApiOperation(value = "修改销售订单", notes = "修改销售订单")
    public Result<?> edit(@RequestBody SaleOrderExpense saleOrderExpense){
        BigDecimal totalAmout = saleOrderExpenseService.saveSaleOrderExpense(saleOrderExpense);
        return Result.ok(totalAmout);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除销售订单")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除销售订单", notes = "通过ID删除销售订单")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {

        SaleOrderExpense saleOrderExpense = saleOrderExpenseService.getById(id);
        saleOrderExpenseService.removeById(id);
        BigDecimal totalAmount = saleOrderExpenseService.updateSaleOrderTotalAmount(saleOrderExpense.getSourceId());
        return Result.ok(totalAmount);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除销售订单", notes = "批量删除销售订单")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        List<String> idList = Arrays.asList(ids.split(","));
        SaleOrderExpense saleOrderExpense = saleOrderExpenseService.getById(idList.get(0));
        this.saleOrderExpenseService.removeByIds(idList);
        BigDecimal totalAmount = saleOrderExpenseService.updateSaleOrderTotalAmount(saleOrderExpense.getSourceId());
        return Result.ok(totalAmount);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询销售订单", notes = "通过ID查询销售订单")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        SaleOrderExpense saleOrderExpense = saleOrderExpenseService.getById(id);
        return Result.ok(saleOrderExpense);
    }

    /**
     * 通过id查询
     *
     * @param sourceId
     * @return
     */
    @GetMapping(value = "/expense/getOne")
    @ApiOperation(value = "通过ID查询销售订单", notes = "通过ID查询销售订单")
    public Result<?> queryByExpenseId(@ApiParam(name = "sourceId", value = "sourceId", required = true) @RequestParam(name = "sourceId", required = true) String sourceId,
                                      @ApiParam(name = "expenseId", value = "expenseId", required = true) @RequestParam(name = "expenseId", required = true) String expenseId) {
        LambdaQueryWrapper<SaleOrderExpense> lambdaQueryWrapper = new LambdaQueryWrapper<SaleOrderExpense>().eq(SaleOrderExpense::getSourceId, sourceId)
                .eq(SaleOrderExpense::getExpenseId, expenseId);
        SaleOrderExpense saleOrderExpense = saleOrderExpenseService.getOne(lambdaQueryWrapper);
        return Result.ok(saleOrderExpense);
    }
}
