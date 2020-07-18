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
import org.jeecg.modules.financial.entity.ReceiptOrder;
import org.jeecg.modules.financial.entity.ReceiptOrderDtl;
import org.jeecg.modules.financial.service.ReceiptOrderDtlService;
import org.jeecg.modules.financial.service.ReceiptOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "收款单明细")
@RestController
@RequestMapping("/receiptOrderDtl")
public class ReceiptOrderDtlController {

    @Autowired
    private ReceiptOrderService receiptOrderService;
    @Autowired
    private ReceiptOrderDtlService receiptOrderDtlService;
    @Autowired
    private ExpenseService expenseService;

    @PostMapping(value = "/add")
    @AutoLog(value = "保存收款明细")
    @ApiOperation(value = "收款明细", notes = "保存收款明细")
    public Result<?> add(@RequestBody ReceiptOrderDtl receiptOrderDtl){

        Result<Object> result = Result.ok();

        if (null == receiptOrderDtl.getPayDate()) {
            receiptOrderDtl.setPayDate(new Date());
        }
        if (StringUtils.isEmpty(receiptOrderDtl.getCompanyId())) {
            receiptOrderDtl.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        if (StringUtils.isEmpty(receiptOrderDtl.getReceiverId())) {
            receiptOrderDtl.setReceiverId(LoginUtils.getLoginUser().getId());
        }
        if (StringUtils.isEmpty(receiptOrderDtl.getSourceId())) {
            ReceiptOrder receiptOrder = receiptOrderService.findBySourceBillId(receiptOrderDtl.getSourceBillId(),
                    receiptOrderDtl.getSourceBillType(), receiptOrderDtl.getCompanyId());
            receiptOrderDtl.setSourceId(receiptOrder.getId());
            receiptOrderDtl.setSourceCode(receiptOrder.getCode());
            receiptOrderDtl.setCompanyId(receiptOrder.getCompanyId());
        }
        receiptOrderDtlService.saveOrUpdate(receiptOrderDtl);
        result.setResult(receiptOrderDtl.getId());

        return result;
    }

    /**
     * 修改
     *
     * @param receiptOrderDtl
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改收款单明细")
    @ApiOperation(value = "修改收款单明细", notes = "修改收款单明细")
    public Result<?> edit(@RequestBody ReceiptOrderDtl receiptOrderDtl){
        if (StringUtils.isBlank(receiptOrderDtl.getCompanyId())) {
            receiptOrderDtl.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        receiptOrderDtlService.saveOrUpdate(receiptOrderDtl);
        return Result.ok("修改成功！");
    }

    /**
     * 分页列表查询
     *
     * @param receiptDtlOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取收款单明细数据列表", notes = "获取所有收款单明细数据列表")
    @GetMapping(value = "/getPage")
//    @PermissionData
    public Result<?> list(ReceiptOrderDtl receiptDtlOrder, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<ReceiptOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(receiptDtlOrder, req.getParameterMap());
        Page<ReceiptOrderDtl> page = new Page<ReceiptOrderDtl>(pageNo, pageSize);

        IPage<ReceiptOrderDtl> pageList = receiptOrderDtlService.page(page, queryWrapper);
        List<ReceiptOrderDtl> receiptOrderDtls = pageList.getRecords();
        handle(receiptOrderDtls);

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
    @AutoLog(value = "删除收款单明细")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除收款单明细", notes = "通过ID删除收款单明细")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        receiptOrderDtlService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除收款单明细", notes = "批量删除收款单明细")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.receiptOrderDtlService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    @ApiOperation(value = "获取收款单明细数据", notes = "获取所有收款单明细数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(ReceiptOrderDtl receiptOrderDtl, HttpServletRequest req) {
        QueryWrapper<ReceiptOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(receiptOrderDtl, req.getParameterMap());
        List<ReceiptOrderDtl> receiptOrderDtls = receiptOrderDtlService.list(queryWrapper);
        handle(receiptOrderDtls);
        return Result.ok(receiptOrderDtls);
    }

    private void handle(List<ReceiptOrderDtl> receiptOrderDtls) {

        if (CollectionUtils.isNotEmpty(receiptOrderDtls)) {

            Map<String, String> expenseMap = new HashMap<>();
            List<String> expenseIds = receiptOrderDtls.stream().map(ReceiptOrderDtl::getExpenseId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(expenseIds)) {
                Collection<Expense> customers = expenseService.listByIds(expenseIds);
                expenseMap.putAll(customers.stream().collect(Collectors.toMap(Expense::getId, Expense::getName)));
            }
            receiptOrderDtls.stream().forEach(o->{
                o.setExpenseName(expenseMap.get(o.getExpenseId()));
                o.setPayTypeName(PayMode.getName(o.getPayType()));
                o.setSourceBillTypeName(BillType.getName(o.getSourceBillType()));
            });
        }
    }
}
