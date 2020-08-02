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
import org.jeecg.modules.financial.entity.RefundOrder;
import org.jeecg.modules.financial.service.RefundOrderService;
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
@Api(tags = "退款单")
@RestController
@RequestMapping("/refundOrder")
public class RefundOrderController {

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 分页列表查询
     *
     * @param refundOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取退款单数据列表", notes = "获取所有退款单数据列表")
    @GetMapping(value = "/getPage")
//    @PermissionData
    public Result<?> list(RefundOrder refundOrder, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<RefundOrder> queryWrapper = QueryGenerator.initQueryWrapper(refundOrder, req.getParameterMap());
        Page<RefundOrder> page = new Page<RefundOrder>(pageNo, pageSize);

        IPage<RefundOrder> pageList = refundOrderService.page(page, queryWrapper);
        List<RefundOrder> refundOrders = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(refundOrders)) {

            Map<String, String> payerMap = new HashMap<>();
            Map<String, String> salemanMap = new HashMap<>();
            List<String> payerIds = refundOrders.stream().map(RefundOrder::getPayerId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(payerIds)) {
                if (refundOrders.get(0).getSourceBillType() == BillType.SALERETURNORDER.getId()) {
                    Collection<Customer> customers = customerService.listByIds(payerIds);
                    payerMap.putAll(customers.stream().collect(Collectors.toMap(Customer::getId, Customer::getName)));
                }
            }

            List<String> salemanIds = refundOrders.stream().map(RefundOrder::getSalemanId).collect(Collectors.toList());
            Collection<SysUser> salemanList = sysUserService.listByIds(salemanIds);
            salemanMap.putAll(salemanList.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getRealname)));
            refundOrders.stream().forEach(o->{
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
