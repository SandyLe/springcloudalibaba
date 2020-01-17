package org.jeecg.modules.saleorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.enums.BillStatus;
import org.jeecg.modules.basic.enums.EnumConvertUtils;
import org.jeecg.modules.basic.service.CustomerService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.saleorder.entity.SaleOrderDeliveryInfo;
import org.jeecg.modules.saleorder.service.SaleOrderDeliveryInfoService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "单表销售订单发货信息")
@RestController
@RequestMapping("/saleOrderDeliveryInfo")
public class SaleOrderDeliveryController {

    @Autowired
    private SaleOrderDeliveryInfoService saleOrderDeliveryInfoService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private ISysDictService iSysDictService;
    /**
     * 添加
     *
     * @param saleOrderDeliveryInfo
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加销售订单发货信息")
    @ApiOperation(value = "添加销售订单发货信息", notes = "添加销售订单发货信息")
    public Result<?> add(@RequestBody SaleOrderDeliveryInfo saleOrderDeliveryInfo) {
        saleOrderDeliveryInfoService.save(saleOrderDeliveryInfo);
        return Result.ok(saleOrderDeliveryInfo.getId());
    }
    /**
     * 获取所有数据
     *
     * @param saleOrderDeliveryInfo
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单发货信息数据", notes = "获取所有销售订单发货信息数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrderDeliveryInfo saleOrderDeliveryInfo, HttpServletRequest req) {
        QueryWrapper<SaleOrderDeliveryInfo> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderDeliveryInfo, req.getParameterMap());
        List<SaleOrderDeliveryInfo> list = saleOrderDeliveryInfoService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param saleOrderDeliveryInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单发货信息数据列表", notes = "获取所有销售订单发货信息数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(SaleOrderDeliveryInfo saleOrderDeliveryInfo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SaleOrderDeliveryInfo> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderDeliveryInfo, req.getParameterMap());
        Page<SaleOrderDeliveryInfo> page = new Page<SaleOrderDeliveryInfo>(pageNo, pageSize);
        IPage<SaleOrderDeliveryInfo> pageList = saleOrderDeliveryInfoService.page(page, queryWrapper);
        List<SaleOrderDeliveryInfo> deliveryInfoList = pageList.getRecords();
        List<String> customerIds = deliveryInfoList.stream().map(SaleOrderDeliveryInfo::getCdiSourceId).collect(Collectors.toList());
        List<String> warehouseIds = deliveryInfoList.stream().map(SaleOrderDeliveryInfo::getWarehouseId).collect(Collectors.toList());
        Collection<Customer> customers = customerService.listByIds(customerIds);
        Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
        Collection<DictModel> sysDict = iSysDictService.queryDictItemsByCode("delivery_type");
        Map<String, String> customerMap = customers.stream().collect(Collectors.toMap(Customer::getId, Customer::getName));
        Map<String, String> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse:: getId, Warehouse:: getName));
        Map<String, String> dictModelMap = sysDict.stream().collect(Collectors.toMap(DictModel::getValue, DictModel::getText));
        deliveryInfoList.stream().forEach(o->{
            o.setWarehouse(warehouseMap.get(o.getWarehouseId()));
            o.setBillStatusName(EnumConvertUtils.getName(BillStatus.class, o.getBillStatus()));
            o.setCustomer(customerMap.get(o.getCdiSourceId()));
            o.setCdiDefaultTypeName(dictModelMap.get(o.getCdiDefaultType()));
        });
        pageList.setRecords(deliveryInfoList);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 修改
     *
     * @param saleOrderDeliveryInfo
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改销售订单发货信息")
    @ApiOperation(value = "修改销售订单发货信息", notes = "修改销售订单发货信息")
    public Result<?> edit(@RequestBody SaleOrderDeliveryInfo saleOrderDeliveryInfo){
        saleOrderDeliveryInfoService.updateById(saleOrderDeliveryInfo);
        return Result.ok(saleOrderDeliveryInfo);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除销售订单发货信息")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除销售订单发货信息", notes = "通过ID删除销售订单发货信息")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        saleOrderDeliveryInfoService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除销售订单发货信息", notes = "批量删除销售订单发货信息")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.saleOrderDeliveryInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

}
