package org.jeecg.modules.saleorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.basic.dto.DeliveryEditDto;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.enums.BillStatus;
import org.jeecg.modules.basic.service.CustomerService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderDeliveryInfo;
import org.jeecg.modules.saleorder.service.SaleOrderDeliveryInfoService;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.jeecg.modules.system.service.ISysDictService;
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
@RequestMapping("/saleOrder")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private SaleOrderDeliveryInfoService saleOrderDeliveryInfoService;
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

        saleOrder.setBillStatus(BillStatus.NEW.getId());
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
    public Result<?> list(SaleOrder saleOrder, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SaleOrder> queryWrapper = QueryGenerator.initQueryWrapper(saleOrder, req.getParameterMap());
        Page<SaleOrder> page = new Page<SaleOrder>(pageNo, pageSize);

        IPage<SaleOrder> pageList = saleOrderService.page(page, queryWrapper);
        List<SaleOrder> saleOrderList = pageList.getRecords();
        List<String> customerIds = saleOrderList.stream().map(SaleOrder::getCustomerId).collect(Collectors.toList());
        List<String> warehouseIds = saleOrderList.stream().map(SaleOrder::getWarehouseId).collect(Collectors.toList());
        Collection<Customer> customers = customerService.listByIds(customerIds);
        Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
        Collection<DictModel> sysDict = iSysDictService.queryDictItemsByCode("receipt_type");
        Collection<DictModel> channelDicts = iSysDictService.queryDictItemsByCode("channel");
        Map<String, String> customerMap = customers.stream().collect(Collectors.toMap(Customer::getId, Customer::getName));
        Map<String, String> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse:: getId, Warehouse:: getName));
        Map<String, String> dictModelMap = sysDict.stream().collect(Collectors.toMap(DictModel::getValue, DictModel::getText));
        Map<String, String> channelMap = channelDicts.stream().collect(Collectors.toMap(DictModel::getValue, DictModel::getText));
        saleOrderList.stream().forEach(o->{
            o.setWarehouse(warehouseMap.get(o.getWarehouseId()));
            o.setCustomer(customerMap.get(o.getCustomerId()));
            o.setReceiptTypeName(dictModelMap.get(o.getReceiptType()));
            o.setChannel(channelMap.get(o.getChannelId()));
            o.setBillStatusName(BillStatus.getName(o.getBillStatus()));
        });

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
        if (null != saleOrder.getTotalamount() && null != saleOrder.getPayamount() && saleOrder.getPayamount().compareTo(BigDecimal.ZERO) > 0) {
            saleOrder.setBillStatus(saleOrder.getTotalamount().compareTo(saleOrder.getPayamount()) <= 0 ? BillStatus.PAID.getId() : BillStatus.PARTICIALPAYMENT.getId());
        }
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
        return Result.ok(saleOrder);
    }

    /**
     * 发货
     *
     * @param deliveryEditDto
     * @return
     */
    @PostMapping(value = "/delivery")
    @AutoLog(value = "发货")
    @ApiOperation(value = "发货", notes = "发货")
    public Result<?> edit(@RequestBody DeliveryEditDto deliveryEditDto) throws Exception {
        String billId = deliveryEditDto.getId();
        SaleOrder saleOrder = saleOrderService.getById(billId);
        saleOrder.setWarehouseId(deliveryEditDto.getWarehouseId());
        saleOrder.setDeliveryTime(deliveryEditDto.getDeliveryTime());
        saleOrder.setInstallTime(deliveryEditDto.getInstallTime());
        saleOrderService.updateById(saleOrder);

        SaleOrderDeliveryInfo cdi = new SaleOrderDeliveryInfo();
        BeanUtils.copyProperties(cdi,deliveryEditDto);
        cdi.setCdiSourceId(saleOrder.getId());
        cdi.setId(deliveryEditDto.getCdiId());
        cdi.setCreateBy(deliveryEditDto.getCdiCreateBy());
        cdi.setCreateTime(deliveryEditDto.getCdiCreateTime());
        cdi.setUpdateBy(deliveryEditDto.getCdiUpdateBy());
        cdi.setUpdateTime(deliveryEditDto.getCdiUpdateTime());
        cdi.setCode(deliveryEditDto.getCdiCode());
        cdi.setName(deliveryEditDto.getCdiName());
        cdi.setRowSts(deliveryEditDto.getCdiRowSts());
        cdi.setSort(deliveryEditDto.getCdiSort());
        cdi.setCode(deliveryEditDto.getCdiContent());
        saleOrderDeliveryInfoService.saveOrUpdate(cdi);

        Result<Object> result = Result.ok();
        result.setResult(saleOrder);
        return result;
    }

}
