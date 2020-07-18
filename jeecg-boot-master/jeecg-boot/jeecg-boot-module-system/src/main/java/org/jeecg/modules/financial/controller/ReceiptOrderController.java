package org.jeecg.modules.financial.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.service.CustomerService;
import org.jeecg.modules.financial.entity.ReceiptOrder;
import org.jeecg.modules.financial.service.ReceiptOrderService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "收款单")
@RestController
@RequestMapping("/receiptOrder")
public class ReceiptOrderController {

    @Autowired
    private ReceiptOrderService receiptOrderService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ISysUserService sysUserService;

    @PostMapping(value = "/create")
    @AutoLog(value = "生成/更新收款单")
    @ApiOperation(value = "生成/更新收款单", notes = "生成/更新收款单")
    public Result<?> add(@RequestBody ReceiptOrder receipt){

        Result<Object> result = Result.ok();
        String id = receiptOrderService.createReceipt(receipt.getSourceId(), receipt.getSourceBillType(),
                LoginUtils.getLoginUser().getCompanyId());
        result.setResult(id);

        return result;
    }

    /**
     * 分页列表查询
     *
     * @param receiptOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取收款单数据列表", notes = "获取所有收款单数据列表")
    @GetMapping(value = "/getPage")
//    @PermissionData
    public Result<?> list(ReceiptOrder receiptOrder, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<ReceiptOrder> queryWrapper = QueryGenerator.initQueryWrapper(receiptOrder, req.getParameterMap());
        Page<ReceiptOrder> page = new Page<ReceiptOrder>(pageNo, pageSize);

        IPage<ReceiptOrder> pageList = receiptOrderService.page(page, queryWrapper);
        List<ReceiptOrder> receiptOrders = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(receiptOrders)) {

            Map<String, String> payerMap = new HashMap<>();
            Map<String, String> salemanMap = new HashMap<>();
            List<String> payerIds = receiptOrders.stream().map(ReceiptOrder::getPayerId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(payerIds)) {
                if (receiptOrders.get(0).getSourceBillType() == BillType.SALEORDER.getId()) {
                    Collection<Customer> customers = customerService.listByIds(payerIds);
                    payerMap.putAll(customers.stream().collect(Collectors.toMap(Customer::getId, Customer::getName)));
                }
            }

            List<String> salemanIds = receiptOrders.stream().map(ReceiptOrder::getSalemanId).collect(Collectors.toList());
            Collection<SysUser> salemanList = sysUserService.listByIds(salemanIds);
            salemanMap.putAll(salemanList.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getRealname)));
            receiptOrders.stream().forEach(o->{
                o.setPayerName(payerMap.get(o.getPayerId()));
                o.setBillStatusName(BillStatus.getName(o.getBillStatusId()));
                o.setSalemanName(salemanMap.get(o.getSalemanId()));
                o.setSourceBillTypeName(BillType.getName(o.getSourceBillType()));
            });
        }

        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }
}
