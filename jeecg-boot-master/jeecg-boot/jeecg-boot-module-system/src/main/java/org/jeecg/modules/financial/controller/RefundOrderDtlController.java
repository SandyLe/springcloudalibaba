package org.jeecg.modules.financial.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.PayMode;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.entity.Expense;
import org.jeecg.modules.basic.service.ExpenseService;
import org.jeecg.modules.financial.entity.RefundOrder;
import org.jeecg.modules.financial.entity.RefundOrderDtl;
import org.jeecg.modules.financial.service.RefundOrderDtlService;
import org.jeecg.modules.financial.service.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "退款单明细")
@RestController
@RequestMapping("/refundOrderDtl")
public class RefundOrderDtlController {

    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private RefundOrderDtlService refundOrderDtlService;
    @Autowired
    private ExpenseService expenseService;

    @PostMapping(value = "/add")
    @AutoLog(value = "保存退款明细")
    @ApiOperation(value = "退款明细", notes = "保存退款明细")
    public Result<?> add(@RequestBody RefundOrderDtl refundOrderDtl){

        Result<Object> result = Result.ok();

        if (null == refundOrderDtl.getPayDate()) {
            refundOrderDtl.setPayDate(new Date());
        }
        if (StringUtils.isEmpty(refundOrderDtl.getCompanyId())) {
            refundOrderDtl.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        if (StringUtils.isEmpty(refundOrderDtl.getReceiverId())) {
            refundOrderDtl.setReceiverId(LoginUtils.getLoginUser().getId());
        }
        if (StringUtils.isEmpty(refundOrderDtl.getSourceId())) {
            RefundOrder refundOrder = refundOrderService.findBySourceBillId(refundOrderDtl.getSourceBillId(),
                    refundOrderDtl.getSourceBillType(), refundOrderDtl.getCompanyId());
            refundOrderDtl.setSourceId(refundOrder.getId());
            refundOrderDtl.setSourceCode(refundOrder.getCode());
            refundOrderDtl.setCompanyId(refundOrder.getCompanyId());
        }
        refundOrderDtlService.saveOrUpdate(refundOrderDtl);
        result.setResult(refundOrderDtl.getId());

        return result;
    }

    /**
     * 修改
     *
     * @param refundOrderDtl
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改退款单明细")
    @ApiOperation(value = "修改退款单明细", notes = "修改退款单明细")
    public Result<?> edit(@RequestBody RefundOrderDtl refundOrderDtl){
        if (StringUtils.isBlank(refundOrderDtl.getCompanyId())) {
            refundOrderDtl.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        refundOrderDtlService.saveOrUpdate(refundOrderDtl);
        return Result.ok("修改成功！");
    }

    /**
     * 分页列表查询
     *
     * @param refundDtlOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取退款单明细数据列表", notes = "获取所有退款单明细数据列表")
    @GetMapping(value = "/getPage")
//    @PermissionData
    public Result<?> list(RefundOrderDtl refundDtlOrder, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<RefundOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(refundDtlOrder, req.getParameterMap());
        Page<RefundOrderDtl> page = new Page<RefundOrderDtl>(pageNo, pageSize);

        IPage<RefundOrderDtl> pageList = refundOrderDtlService.page(page, queryWrapper);
        List<RefundOrderDtl> refundOrderDtls = pageList.getRecords();
        handle(refundOrderDtls);

        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除退款单明细")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除退款单明细", notes = "通过ID删除退款单明细")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {

        RefundOrderDtl dtl = refundOrderDtlService.getById(id);
        refundOrderDtlService.removeById(id);
        refundOrderDtlService.updateBillStatus(dtl);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除退款单明细", notes = "批量删除退款单明细")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.refundOrderDtlService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    @ApiOperation(value = "获取退款单明细数据", notes = "获取所有退款单明细数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(RefundOrderDtl refundOrderDtl, HttpServletRequest req) {
        QueryWrapper<RefundOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(refundOrderDtl, req.getParameterMap());
        List<RefundOrderDtl> refundOrderDtls = refundOrderDtlService.list(queryWrapper);
        handle(refundOrderDtls);
        return Result.ok(refundOrderDtls);
    }

    private void handle(List<RefundOrderDtl> refundOrderDtls) {

        if (CollectionUtils.isNotEmpty(refundOrderDtls)) {

            Map<String, String> expenseMap = new HashMap<>();
            List<String> expenseIds = refundOrderDtls.stream().map(RefundOrderDtl::getExpenseId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(expenseIds)) {
                Collection<Expense> customers = expenseService.listByIds(expenseIds);
                expenseMap.putAll(customers.stream().collect(Collectors.toMap(Expense::getId, Expense::getName)));
            }
            refundOrderDtls.stream().forEach(o->{
                o.setExpenseName(expenseMap.get(o.getExpenseId()));
                o.setPayTypeName(PayMode.getName(o.getPayType()));
                o.setSourceBillTypeName(BillType.getName(o.getSourceBillType()));
            });
        }
    }
}
