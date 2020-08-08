package org.jeecg.modules.saleorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.enums.RefundOrderStatus;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.CustomerService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.financial.entity.RefundOrder;
import org.jeecg.modules.financial.service.RefundOrderService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.saleorder.dto.SaleOrderReturnDto;
import org.jeecg.modules.saleorder.entity.*;
import org.jeecg.modules.saleorder.service.*;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "销售退货")
@RestController
@RequestMapping("/saleOrderReturn")
public class SaleOrderReturnController {

    @Autowired
    private SaleOrderReturnService saleOrderReturnService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private SaleOrderReturnMtlService saleOrderReturnMtlService;
    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private SaleOrderMtlService saleOrderMtlService;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private SaleOrderChannelService saleOrderChannelService;


    /**
     * 添加
     *
     * @param saleOrderReturn
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加销售退货")
    @ApiOperation(value = "添加销售退货", notes = "添加销售退货")
    public Result<?> add(@RequestBody SaleOrderReturn saleOrderReturn) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(saleOrderReturn.getCompanyId())) {
            saleOrderReturn.setCompanyId(sysUser.getCompanyId());
        }
        saleOrderReturn.setBillStatus(BillStatus.NEW.getId());
        saleOrderReturn.setCode(billCodeBuilderService.getBillCode(BillType.SALERETURNORDER.getId()));
        SaleOrderReturn exists = saleOrderReturnService.getOne(new LambdaQueryWrapper<SaleOrderReturn>().eq(SaleOrderReturn::getCode, saleOrderReturn.getCode()).ne(SaleOrderReturn::getId, saleOrderReturn.getId()));
        Assert.isNull(exists, "单号已存在！");
        saleOrderReturnService.save(saleOrderReturn);
        Result<Object> result = Result.ok();
        result.setResult(saleOrderReturn);
        return result;
    }

    /**
     * 保存
     *
     * @param saleOrderReturnDto
     * @return
     */
    @PostMapping(value = "/save")
    @AutoLog(value = "保存销售退货")
    @ApiOperation(value = "保存销售退货", notes = "保存销售退货")
    public Result<?> save(@RequestBody SaleOrderReturnDto saleOrderReturnDto){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(saleOrderReturnDto.getCompanyId())) {
            saleOrderReturnDto.setCompanyId(sysUser.getCompanyId());
        }
        if (StringUtils.isBlank(saleOrderReturnDto.getSalemanId())) {
            saleOrderReturnDto.setSalemanId(sysUser.getId());
        }
        Boolean isNew = false;
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (StringUtils.isEmpty(saleOrderReturnDto.getId())) {
            isNew = true;
            saleOrderReturnDto.setBillStatus(BillStatus.NEW.getId());
            saleOrderReturnDto.setCode(billCodeBuilderService.getBillCode(BillType.SALERETURNORDER.getId()));
            saleOrderReturnDto.setRefundStatusId(RefundOrderStatus.ToRefund.getId());
            saleOrderReturnDto.setBillType(BillType.SALERETURNORDER.getId());
        }
        final Boolean newFlag = BooleanUtils.isTrue(isNew);

        // 保存退货单主表信息
        saleOrderReturnService.saveOrUpdate(saleOrderReturnDto);

        // 退货单子表信息
        if (CollectionUtils.isNotEmpty(saleOrderReturnDto.getDetaillist())) {
            for(SaleOrderReturnMtl o : saleOrderReturnDto.getDetaillist()){
                if (BooleanUtils.isTrue(newFlag)) {
                    o.setId(null);
                    o.setCreateBy(null);
                    o.setCreateTime(null);
                    o.setUpdateBy(null);
                    o.setUpdateTime(null);
                }
                o.setSourceId(saleOrderReturnDto.getId());
                saleOrderReturnMtlService.saveOrUpdate(o);
                totalAmount = totalAmount.add(o.getAmount());
            };
        }

        // 退款单
        RefundOrder refundOrder = refundOrderService.findBySourceBillId(saleOrderReturnDto.getId(), saleOrderReturnDto.getBillType(), saleOrderReturnDto.getCompanyId());
        if (null == refundOrder) {
            refundOrder = new RefundOrder();
            refundOrder.setBillStatusId(BillStatus.NEW.getId());
            refundOrder.setAmount(saleOrderReturnDto.getTotalamount());
            refundOrder.setBillDate(new Date());
            refundOrder.setPayerId(saleOrderReturnDto.getCustomerId());
            refundOrder.setSalemanId(saleOrderReturnDto.getSalemanId());
            refundOrder.setSourceBillCode(saleOrderReturnDto.getCode());
            refundOrder.setSourceId(saleOrderReturnDto.getId());
            refundOrder.setSalemanId(saleOrderReturnDto.getSalemanId());
            refundOrder.setSourceBillType(BillType.SALERETURNORDER.getId());
            refundOrder.setCompanyId(saleOrderReturnDto.getCompanyId());
        }
        refundOrderService.saveOrUpdate(refundOrder);

        Result<Object> result = Result.ok();
        result.setResult(saleOrderReturnDto);
        return result;
    }

    /**
     * 获取所有数据
     *
     * @param saleOrderReturn
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售退货数据", notes = "获取所有销售退货数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrderReturn saleOrderReturn, HttpServletRequest req) {
        QueryWrapper<SaleOrderReturn> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderReturn, req.getParameterMap());
        List<SaleOrderReturn> list = saleOrderReturnService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param saleOrderReturn
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售退货数据列表", notes = "获取所有销售退货数据列表")
    @GetMapping(value = "/getPage")
    @PermissionData
    public Result<?> list(SaleOrderReturn saleOrderReturn, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SaleOrderReturn> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderReturn, req.getParameterMap());
        Page<SaleOrderReturn> page = new Page<SaleOrderReturn>(pageNo, pageSize);

        IPage<SaleOrderReturn> pageList = saleOrderReturnService.page(page, queryWrapper);
        List<SaleOrderReturn> saleOrderReturnList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(saleOrderReturnList)) {
            List<String> customerIds = saleOrderReturnList.stream().map(SaleOrderReturn::getCustomerId).collect(Collectors.toList());
            List<String> warehouseIds = saleOrderReturnList.stream().map(SaleOrderReturn::getWarehouseId).collect(Collectors.toList());
            Collection<Customer> customers = customerService.listByIds(customerIds);
            Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
            Collection<DictModel> sysDict = iSysDictService.queryDictItemsByCode("receipt_type");
            List<SaleOrderChannel> channellist = saleOrderChannelService.list();
            Map<String, String> customerMap = customers.stream().collect(Collectors.toMap(Customer::getId, Customer::getName));
            Map<String, String> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse:: getId, Warehouse:: getName));
            Map<String, String> dictModelMap = sysDict.stream().collect(Collectors.toMap(DictModel::getValue, DictModel::getText));
            saleOrderReturnList.stream().forEach(o->{
                o.setWarehouse(warehouseMap.get(o.getWarehouseId()));
                o.setCustomer(customerMap.get(o.getCustomerId()));
                o.setPayTypeName(dictModelMap.get(o.getPayType()));
                o.setBillStatusName(BillStatus.getName(o.getBillStatus()));
                o.setRefundStatusName(RefundOrderStatus.getName(o.getRefundStatusId()));
                Optional<SaleOrderChannel> channel = channellist.stream().filter(p-> StringUtils.equals(p.getId(), o.getChannelId())).findFirst();
                if (channel.isPresent()){
                    o.setChannel(channel.get().getName());
                }
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
     * @param saleOrderReturn
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改销售退货")
    @ApiOperation(value = "修改销售退货", notes = "修改销售退货")
    public Result<?> edit(@RequestBody SaleOrderReturn saleOrderReturn){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(saleOrderReturn.getCompanyId())) {
            saleOrderReturn.setCompanyId(sysUser.getCompanyId());
        }
        SaleOrderReturn exists = saleOrderReturnService.getById(saleOrderReturn.getId());
        SaleOrderReturn existCode = saleOrderReturnService.getOne(new LambdaQueryWrapper<SaleOrderReturn>().eq(SaleOrderReturn::getCode, saleOrderReturn.getCode()).ne(SaleOrderReturn::getId, saleOrderReturn.getId()));
        Assert.isNull(existCode, "单号已存在！");
        saleOrderReturn.setMtlamount(exists.getMtlamount());
        saleOrderReturn.setOtheramount(exists.getOtheramount());
        saleOrderReturn.setTotalamount(exists.getTotalamount());
        saleOrderReturnService.updateById(saleOrderReturn);
        if (saleOrderReturn.getBillStatus() == BillStatus.REFUND.getId()) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setCompanyId(saleOrderReturn.getCompanyId());
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(saleOrderReturn.getWarehouseId());
            inventoryIn.setSourceCode(saleOrderReturn.getCode());
            inventoryIn.setSourceId(saleOrderReturn.getId());
            inventoryIn.setBillType(BillType.INVENTORYIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.SALERETURNORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);

        }

        Result<Object> result = Result.ok();
        result.setResult(saleOrderReturn);
        return result;
    }

    /**
     * 退款
     *
     * @param saleOrderReturn
     * @return
     */
    @PostMapping(value = "/checkin")
    @AutoLog(value = "修改销售退货")
    @ApiOperation(value = "修改销售退货", notes = "修改销售退货")
    public Result<?> checkin(@RequestBody SaleOrderReturn saleOrderReturn){
        if (null != saleOrderReturn.getTotalamount() && null != saleOrderReturn.getPayamount() && saleOrderReturn.getPayamount().compareTo(BigDecimal.ZERO) > 0) {
            saleOrderReturn.setBillStatus(saleOrderReturn.getTotalamount().compareTo(saleOrderReturn.getPayamount()) <= 0 ? BillStatus.PAID.getId() : BillStatus.PARTICIALPAYMENT.getId());
        }
        saleOrderReturnService.updateById(saleOrderReturn);
        Result<Object> result = Result.ok();
        result.setResult(saleOrderReturn);
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除销售退货")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除销售退货", notes = "通过ID删除销售退货")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        saleOrderReturnService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除销售退货", notes = "批量删除销售退货")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.saleOrderReturnService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询销售退货", notes = "通过ID查询销售退货")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        SaleOrderReturn saleOrderReturn = saleOrderReturnService.getById(id);
        Customer customer = customerService.getById(saleOrderReturn.getCustomerId());
        saleOrderReturn.setCustomer(null != customer ? customer.getName() : null);
        List<SaleOrderChannel> channellist = saleOrderChannelService.list();
        Optional<SaleOrderChannel> channel = channellist.stream().filter(p-> StringUtils.equals(p.getId(), saleOrderReturn.getChannelId())).findFirst();
        if (channel.isPresent()){
            saleOrderReturn.setChannel(channel.get().getName());
        }
        return Result.ok(saleOrderReturn);
    }

    /**
     * 通过id查询DTO
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryDto")
    @ApiOperation(value = "通过ID查询销售退货DTO", notes = "通过ID查询销售退货DTO")
    public Result<?> queryDto(@ApiParam(name = "id", value = "示例id", required = false) @RequestParam(name = "id", required = false) String id,
                              @ApiParam(name = "sourceId", value = "原单id", required = false) @RequestParam(name = "sourceId", required = false) String sourceId) throws Exception {

        SaleOrderReturnDto dto = new SaleOrderReturnDto();

        if (StringUtils.isNotBlank(id)) {

            SaleOrderReturn saleOrderReturn = saleOrderReturnService.getById(id);
            Customer customer = customerService.getById(saleOrderReturn.getCustomerId());
            saleOrderReturn.setCustomer(null != customer ? customer.getName() : null);
            BeanUtils.copyProperties(saleOrderReturn, dto);

            List<SaleOrderReturnMtl> mtls = saleOrderReturnMtlService.getList(id);
            dto.setDetaillist(mtls);
        } else if (StringUtils.isNotBlank(sourceId)) {

            SaleOrder saleOrder = saleOrderService.getById(sourceId);
            dto.setCustomerId(saleOrder.getCustomerId());
            dto.setChannelId(saleOrder.getChannelId());
            dto.setSourceCode(saleOrder.getCode());
            dto.setSourceId(saleOrder.getId());
            List<SaleOrderChannel> channellist = saleOrderChannelService.list();
            Optional<SaleOrderChannel> channel = channellist.stream().filter(p-> StringUtils.equals(p.getId(), saleOrder.getChannelId())).findFirst();
            if (channel.isPresent()){
                dto.setChannel(channel.get().getName());
            }
            List<SaleOrderReturnMtl> returnMtls = Lists.newArrayList();
            List<SaleOrderMtl> saleOrderMtls = saleOrderMtlService.findList(sourceId);
            saleOrderMtls.forEach(mtl -> {
                SaleOrderReturnMtl returnMtl = new SaleOrderReturnMtl();
                BeanUtils.copyProperties(mtl, returnMtl,  "createTime", "updateTime", "createBy", "updateBy");
                returnMtls.add(returnMtl);
            });
            dto.setDetaillist(returnMtls);

        }
        return Result.ok(dto);
    }

}
