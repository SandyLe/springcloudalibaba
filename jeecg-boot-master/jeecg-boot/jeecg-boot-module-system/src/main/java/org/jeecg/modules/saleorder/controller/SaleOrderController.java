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
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.enums.DeliveryType;
import org.jeecg.common.enums.ReceiptStatus;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.CustomerService;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderChannel;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.service.SaleOrderChannelService;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "单表销售订单")
@RestController
@RequestMapping("/saleOrder")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    @Autowired
    private SaleOrderMtlService saleOrderMtlService;
    @Autowired
    private SaleOrderChannelService saleOrderChannelService;

    /**
     * 添加
     *
     * @param saleOrder
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加销售订单")
    @ApiOperation(value = "添加销售订单", notes = "添加销售订单")
    public Result<?> add(@RequestBody SaleOrder saleOrder) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(saleOrder.getCompanyId())) {
            saleOrder.setCompanyId(sysUser.getCompanyId());
        }
        if (null == saleOrder.getBillType()) {
            saleOrder.setBillType(BillType.SALEORDER.getId());
        }
        saleOrder.setBillStatus(BillStatus.NEW.getId());
        saleOrder.setCode(billCodeBuilderService.getBillCode(BillType.SALEORDER.getId()));
        SaleOrder exists = saleOrderService.getOne(new LambdaQueryWrapper<SaleOrder>().eq(SaleOrder::getCode, saleOrder.getCode()).ne(SaleOrder::getId, saleOrder.getId()));
        Assert.isNull(exists, "单号已存在！");
        saleOrderService.save(saleOrder);
        Result<Object> result = Result.ok();
        result.setResult(saleOrder);
        return result;
    }
    /**
     * 获取所有数据
     *
     * @param saleOrder
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单数据", notes = "获取所有销售订单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrder saleOrder, HttpServletRequest req) {
        QueryWrapper<SaleOrder> queryWrapper = QueryGenerator.initQueryWrapper(saleOrder, req.getParameterMap());
        List<SaleOrder> list = saleOrderService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param saleOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单数据列表", notes = "获取所有销售订单数据列表")
    @GetMapping(value = "/getPage")
//    @PermissionData
    public Result<?> list(SaleOrder saleOrder, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SaleOrder> queryWrapper = QueryGenerator.initQueryWrapper(saleOrder, req.getParameterMap());
        Page<SaleOrder> page = new Page<SaleOrder>(pageNo, pageSize);

        IPage<SaleOrder> pageList = saleOrderService.page(page, queryWrapper);
        List<SaleOrder> saleOrderList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(saleOrderList)) {
            List<String> customerIds = saleOrderList.stream().map(SaleOrder::getCustomerId).collect(Collectors.toList());
            Collection<Customer> customers = customerService.listByIds(customerIds);
            List<SaleOrderChannel> channellist = saleOrderChannelService.list();
            Map<String, String> customerMap = customers.stream().collect(Collectors.toMap(Customer::getId, Customer::getName));
            saleOrderList.stream().forEach(o->{
                o.setReceiptStatusName(ReceiptStatus.getName(o.getReceiptStatus()));
                o.setCustomer(customerMap.get(o.getCustomerId()));
                o.setDeliveryTypeName(DeliveryType.getName(o.getDeliveryType()));
                Optional<SaleOrderChannel> channel = channellist.stream().filter(p-> StringUtils.equals(p.getId(), o.getChannelId())).findFirst();
                if (channel.isPresent()){
                    o.setChannel(channel.get().getName());
                }
                o.setBillStatusName(BillStatus.getName(o.getBillStatus()));
                o.setReceiptStatusName(ReceiptStatus.getName(o.getReceiptStatus()));
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
     * @param saleOrder
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改销售订单")
    @ApiOperation(value = "修改销售订单", notes = "修改销售订单")
    public Result<?> edit(@RequestBody SaleOrder saleOrder){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(saleOrder.getCompanyId())) {
            saleOrder.setCompanyId(sysUser.getCompanyId());
        }
        if (null == saleOrder.getBillType()) {
            saleOrder.setBillType(BillType.SALEORDER.getId());
        }
        SaleOrder exists = saleOrderService.getById(saleOrder.getId());
        SaleOrder existCode = saleOrderService.getOne(new LambdaQueryWrapper<SaleOrder>().eq(SaleOrder::getCode, saleOrder.getCode()).ne(SaleOrder::getId, saleOrder.getId()));
        Assert.isNull(existCode, "单号已存在！");
        saleOrder.setMtlamount(exists.getMtlamount());
        saleOrder.setOtheramount(exists.getOtheramount());
        saleOrder.setTotalamount(exists.getTotalamount());
        saleOrderService.updateById(saleOrder);
        Result<Object> result = Result.ok();
        result.setResult(saleOrder);
        return result;
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
        saleOrderService.removeById(id);
        return Result.ok("删除成功!");
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
        this.saleOrderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
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
        SaleOrder saleOrder = saleOrderService.getById(id);
        if (null != saleOrder) {
            if (StringUtils.isNotBlank(saleOrder.getCustomerId())) {
                Customer customer = customerService.getById(saleOrder.getCustomerId());
                saleOrder.setCustomer(null != customer ? customer.getName() : null);
            }
            saleOrder.setReceiptStatusName(ReceiptStatus.getName(saleOrder.getReceiptStatus()));
            saleOrder.setDeliveryTypeName(DeliveryType.getName(saleOrder.getDeliveryType()));
            List<SaleOrderChannel> channellist = saleOrderChannelService.list();
            Optional<SaleOrderChannel> channel = channellist.stream().filter(p-> StringUtils.equals(p.getId(), saleOrder.getChannelId())).findFirst();
            if (channel.isPresent()){
                saleOrder.setChannel(channel.get().getName());
            }
        }
        return Result.ok(saleOrder);
    }

    /**
     * 通过code查询
     *
     * @param code
     * @return
     */
    @GetMapping(value = "/getOneByCode")
    @ApiOperation(value = "通过Code查询销售订单", notes = "通过Code查询销售订单")
    public Result<?> queryByCode(@ApiParam(name = "code", value = "示例code", required = true) @RequestParam(name = "code", required = true) String code) {
        SaleOrder saleOrder = saleOrderService.getOne(new LambdaQueryWrapper<SaleOrder>().eq(SaleOrder::getCode, code));
        if (null != saleOrder) {
            if (StringUtils.isNotBlank(saleOrder.getCustomerId())) {
                Customer customer = customerService.getById(saleOrder.getCustomerId());
                saleOrder.setCustomer(null != customer ? customer.getName() : null);
            }
            saleOrder.setReceiptStatusName(ReceiptStatus.getName(saleOrder.getReceiptStatus()));
            saleOrder.setDeliveryTypeName(DeliveryType.getName(saleOrder.getDeliveryType()));
        }
        return Result.ok(saleOrder);
    }

    /**
     * 作废
     *
     * @param saleOrder
     * @return
     */
    @PostMapping(value = "/disable")
    @AutoLog(value = "修改销售订单")
    @ApiOperation(value = "修改销售订单", notes = "修改销售订单")
    public Result<?> disable(@RequestBody SaleOrder saleOrder){

        SaleOrder existed = saleOrderService.getById(saleOrder.getId());
        Assert.notNull(existed, "销售订单不存在！");
        existed.setBillStatus(BillStatus.INVALID.getId());
        saleOrderService.updateById(existed);

        List<SaleOrderMtl> saleOrderMtls = saleOrderMtlService.list(new LambdaQueryWrapper<SaleOrderMtl>().eq(SaleOrderMtl::getSourceId, saleOrder.getId()));
        if (CollectionUtils.isNotEmpty(saleOrderMtls)) {
            saleOrderMtls.stream().forEach(o->{o.setBillStatus(BillStatus.INVALID.getId());});
            saleOrderMtlService.updateBatchById(saleOrderMtls);
        }

        List<InventoryOut> inventoryOuts = inventoryOutService.list(new LambdaQueryWrapper<InventoryOut>().eq(InventoryOut::getSourceId, saleOrder.getId()));
        if (CollectionUtils.isNotEmpty(inventoryOuts)) {
            inventoryOuts.stream().forEach(o->{o.setBillStatus(BillStatus.INVALID.getId());});
            inventoryOutService.updateBatchById(inventoryOuts);
        }

        return Result.ok("修改成功！");
    }
}
