package org.jeecg.modules.saleorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
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
@Api(tags = "??????????????????")
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
     * ??????
     *
     * @param saleOrder
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "??????????????????")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
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
        Assert.isNull(exists, "??????????????????");
        saleOrderService.save(saleOrder);
        Result<Object> result = Result.ok();
        result.setResult(saleOrder);
        return result;
    }
    /**
     * ??????????????????
     *
     * @param saleOrder
     * @param req
     * @return
     */
    @ApiOperation(value = "????????????????????????", notes = "??????????????????????????????")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrder saleOrder, HttpServletRequest req) {
        QueryWrapper<SaleOrder> queryWrapper = QueryGenerator.initQueryWrapper(saleOrder, req.getParameterMap());
        List<SaleOrder> list = saleOrderService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * ??????????????????
     *
     * @param saleOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "??????????????????????????????", notes = "????????????????????????????????????")
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

        log.info("??????????????????" + pageList.getCurrent());
        log.info("????????????????????????" + pageList.getSize());
        log.info("?????????????????????" + pageList.getRecords().size());
        log.info("???????????????" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * ??????
     *
     * @param saleOrder
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "??????????????????")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
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
        Assert.isNull(existCode, "??????????????????");
        saleOrder.setMtlamount(exists.getMtlamount());
        saleOrder.setOtheramount(exists.getOtheramount());
        saleOrder.setTotalamount(exists.getTotalamount());
        saleOrderService.updateById(saleOrder);
        Result<Object> result = Result.ok();
        result.setResult(saleOrder);
        return result;
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "??????????????????")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "??????ID??????????????????", notes = "??????ID??????????????????")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        saleOrderService.removeById(id);
        return Result.ok("????????????!");
    }

    /**
     * ????????????
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.saleOrderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("?????????????????????");
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "??????ID??????????????????", notes = "??????ID??????????????????")
    public Result<?> queryById(@ApiParam(name = "id", value = "??????id", required = true) @RequestParam(name = "id", required = true) String id) {
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
     * ??????code??????
     *
     * @param code
     * @return
     */
    @GetMapping(value = "/getOneByCode")
    @ApiOperation(value = "??????Code??????????????????", notes = "??????Code??????????????????")
    public Result<?> queryByCode(@ApiParam(name = "code", value = "??????code", required = true) @RequestParam(name = "code", required = true) String code) {
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
     * ??????
     *
     * @param saleOrder
     * @return
     */
    @PostMapping(value = "/disable")
    @AutoLog(value = "??????????????????")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public Result<?> disable(@RequestBody SaleOrder saleOrder){

        SaleOrder existed = saleOrderService.getById(saleOrder.getId());
        Assert.notNull(existed, "????????????????????????");
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

        return Result.ok("???????????????");
    }

    /**
     * ????????????????????????
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getTaobaoOrder")
    @ApiOperation(value = "??????ID??????????????????", notes = "??????ID??????????????????")
    public Result<?> getTaobaoOrder() throws Exception {

        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "31020098", "b05879570f97a1fbf84d35293c12500b");
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        req.setFields("tid,type,status,payment,orders,rx_audit_status");
        req.setStartCreated(com.taobao.api.internal.util.StringUtils.parseDateTime("2020-08-12 00:00:00"));
        req.setEndCreated(com.taobao.api.internal.util.StringUtils.parseDateTime("2020-08-13 23:59:59"));
        /*req.setStatus("ALL_WAIT_PAY");
        req.setBuyerNick("zhangsan");
        req.setType("game_equipment");
        req.setExtType("service");
        req.setRateStatus("RATE_UNBUYER");
        req.setTag("time_card");*/
        req.setPageNo(1L);
        req.setPageSize(40L);
        req.setUseHasNext(true);
//        req.setBuyerOpenId("AAHm5d-EAAeGwJedwSHpg8bT");
        TradesSoldGetResponse rsp = client.execute(req, "6101107c9971e731a27e8bf4179f36b10341f340637e9c62204151847931");
        System.out.println(rsp.getBody());


        return Result.ok(rsp.getBody());
    }

}
