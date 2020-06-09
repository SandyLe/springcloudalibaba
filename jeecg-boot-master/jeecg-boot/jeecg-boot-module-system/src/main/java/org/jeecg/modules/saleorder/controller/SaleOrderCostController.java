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
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.entity.Expense;
import org.jeecg.modules.basic.entity.ServiceInstitution;
import org.jeecg.modules.basic.service.ExpenseService;
import org.jeecg.modules.basic.service.ServiceInstitutionService;
import org.jeecg.modules.saleorder.entity.SaleOrderCost;
import org.jeecg.modules.saleorder.service.SaleOrderCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "销售订单成本")
@RestController
@RequestMapping("/saleOrderCost")
public class SaleOrderCostController {

    @Autowired
    private SaleOrderCostService saleOrderCostService;
    @Autowired
    private ServiceInstitutionService serviceInstitutionService;
    @Autowired
    private ExpenseService expenseService;
    /**
     * 添加
     *
     * @param saleOrderCost
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加销售订单成本")
    @ApiOperation(value = "添加销售订单成本", notes = "添加销售订单成本")
    public Result<?> add(@RequestBody SaleOrderCost saleOrderCost) {
        if (StringUtils.isBlank(saleOrderCost.getCompanyId())) {
            saleOrderCost.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        saleOrderCost.setBillStatusId(BillStatus.NEW.getId());
        saleOrderCostService.saveOrUpdate(saleOrderCost);
        return Result.ok("添加成功！");
    }
    /**
     * 获取所有数据
     *
     * @param saleOrderCost
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单成本数据", notes = "获取所有销售订单成本数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrderCost saleOrderCost, HttpServletRequest req) {
        QueryWrapper<SaleOrderCost> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderCost, req.getParameterMap());
        List<SaleOrderCost> saleOrderCosts = saleOrderCostService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(saleOrderCosts)) {
            List<String> expenseIds = saleOrderCosts.stream().map(SaleOrderCost::getExpenseId).collect(Collectors.toList());
            Collection<Expense> expenses = expenseService.listByIds(expenseIds);
            Map<String, String> expenseMap = expenses.stream().collect(Collectors.toMap(Expense::getId, Expense::getName));
            List<String> institutionIds = saleOrderCosts.stream().map(SaleOrderCost::getPayeeId).collect(Collectors.toList());
            Collection<ServiceInstitution> institutions = serviceInstitutionService.listByIds(institutionIds);
            Map<String, String> institutionMap = institutions.stream().collect(Collectors.toMap(ServiceInstitution::getId, ServiceInstitution::getName));
            saleOrderCosts.stream().forEach(o->{
                o.setExpense(expenseMap.get(o.getExpenseId()));
                o.setPayee(institutionMap.get(o.getPayeeId()));
                o.setBillStatus(EnumConvertUtils.getName(BillStatus.class, o.getBillStatusId()));
            });
        }
        return Result.ok(saleOrderCosts);
    }
    /**
     * 分页列表查询
     *
     * @param saleOrderCost
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单成本数据列表", notes = "获取所有销售订单成本数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(SaleOrderCost saleOrderCost, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SaleOrderCost> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderCost, req.getParameterMap());
        Page<SaleOrderCost> page = new Page<SaleOrderCost>(pageNo, pageSize);
        IPage<SaleOrderCost> pageList = saleOrderCostService.page(page, queryWrapper);
        List<SaleOrderCost> saleOrderCostList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(saleOrderCostList)) {
            List<String> expenseIds = saleOrderCostList.stream().map(SaleOrderCost::getExpenseId).collect(Collectors.toList());
            Collection<Expense> expenses = expenseService.listByIds(expenseIds);
            Map<String, String> expenseMap = expenses.stream().collect(Collectors.toMap(Expense::getId, Expense::getName));
            List<String> institutionIds = saleOrderCostList.stream().map(SaleOrderCost::getPayeeId).collect(Collectors.toList());
            Collection<ServiceInstitution> institutions = serviceInstitutionService.listByIds(institutionIds);
            Map<String, String> institutionMap = institutions.stream().collect(Collectors.toMap(ServiceInstitution::getId, ServiceInstitution::getName));
            saleOrderCostList.stream().forEach(o->{
                o.setExpense(expenseMap.get(o.getExpenseId()));
                o.setPayee(institutionMap.get(o.getPayeeId()));
                o.setBillStatus(EnumConvertUtils.getName(BillStatus.class, o.getBillStatusId()));
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
     * @param saleOrderCost
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改销售订单成本")
    @ApiOperation(value = "修改销售订单成本", notes = "修改销售订单成本")
    public Result<?> edit(@RequestBody SaleOrderCost saleOrderCost){
        if (StringUtils.isBlank(saleOrderCost.getCompanyId())) {
            saleOrderCost.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        saleOrderCostService.saveOrUpdate(saleOrderCost);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除销售订单成本")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除销售订单成本", notes = "通过ID删除销售订单成本")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {

        saleOrderCostService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除销售订单成本", notes = "批量删除销售订单成本")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        List<String> idList = Arrays.asList(ids.split(","));
        this.saleOrderCostService.removeByIds(idList);
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id处理
     *
     * @param id
     * @return
     */
    @AutoLog(value = "处理销售订单成本")
    @GetMapping(value = "/handle")
    @ApiOperation(value = "通过ID处理销售订单成本", notes = "通过ID处理销售订单成本")
    public Result<?> handle(@RequestParam(name = "id", required = true) String id) {

        SaleOrderCost saleOrderCost = saleOrderCostService.getById(id);
        saleOrderCost.setBillStatusId(BillStatus.DOWN.getId());
        saleOrderCostService.saveOrUpdate(saleOrderCost);
        return Result.ok("处理成功!");
    }

    /**
     * 批量处理
     *
     * @param ids
     * @return
     */
    @GetMapping(value = "/handleBatch")
    @ApiOperation(value = "批量处理销售订单成本", notes = "批量处理销售订单成本")
    public Result<?> handleBatch(@RequestParam(name = "ids", required = true) String ids) {

        List<String> idList = Arrays.asList(ids.split(","));
        LambdaQueryWrapper<SaleOrderCost> saleOrderCostLambdaQueryWrapper = new LambdaQueryWrapper<SaleOrderCost>();
        saleOrderCostLambdaQueryWrapper.in(SaleOrderCost::getId, idList);
        List<SaleOrderCost> saleOrderCosts = saleOrderCostService.list(saleOrderCostLambdaQueryWrapper);
        saleOrderCosts.stream().forEach(o->{
            o.setBillStatusId(BillStatus.DOWN.getId());
        });
        this.saleOrderCostService.updateBatchById(saleOrderCosts);
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询销售订单成本", notes = "通过ID查询销售订单成本")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        SaleOrderCost saleOrderCost = saleOrderCostService.getById(id);
        return Result.ok(saleOrderCost);
    }

    /**
     * 通过id查询
     *
     * @param sourceId
     * @return
     */
    @GetMapping(value = "/expense/getOne")
    @ApiOperation(value = "通过ID查询销售订单成本", notes = "通过ID查询销售订单成本")
    public Result<?> queryByExpenseId(@ApiParam(name = "sourceId", value = "sourceId", required = true) @RequestParam(name = "sourceId", required = true) String sourceId,
                                      @ApiParam(name = "expenseId", value = "expenseId", required = true) @RequestParam(name = "expenseId", required = true) String expenseId) {
        LambdaQueryWrapper<SaleOrderCost> lambdaQueryWrapper = new LambdaQueryWrapper<SaleOrderCost>().eq(SaleOrderCost::getSourceId, sourceId)
                .eq(SaleOrderCost::getExpenseId, expenseId);
        SaleOrderCost saleOrderCost = saleOrderCostService.getOne(lambdaQueryWrapper);
        return Result.ok(saleOrderCost);
    }
}
