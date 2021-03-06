package org.jeecg.modules.inventory.controller;

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
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialAuxiliary;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.*;
//import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.financial.entity.ReceiptOrder;
import org.jeecg.modules.financial.service.ReceiptOrderService;
import org.jeecg.modules.inventory.dto.PreInventoryDto;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "出库单")
@RestController
@RequestMapping("/inventoryOut")
public class InventoryOutController {

    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private MaterialUnitService materialUnitService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialAuxiliaryService materialAuxiliaryService;
    @Autowired
    private ReceiptOrderService receiptOrderService;

    /**
     * 出库
     *
     * @param inventoryOut
     * @return
     */
    @PostMapping(value = "/save")
    @AutoLog(value = "添加出库单")
    @ApiOperation(value = "添加出库单", notes = "添加出库单")
    public Result<?> add(@RequestBody InventoryOut inventoryOut, HttpServletRequest req) {

        InventoryOut dbInventoryOut = inventoryOutService.queryBySourceId (inventoryOut.getSourceBillType(), inventoryOut.getSourceId());
        if (null != dbInventoryOut ) {
            inventoryOutService.deleteBySourceId(dbInventoryOut.getSourceBillType(), inventoryOut.getSourceId());
        }

        if (StringUtils.isBlank(inventoryOut.getCompanyId())) {
            inventoryOut.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        if (null == inventoryOut.getBillType()) {
            inventoryOut.setBillType(BillType.INVENTORYOUT.getId());
        }
        if (null == inventoryOut.getBillStatus()) {
            inventoryOut.setBillStatus(BillStatus.TOSEND.getId());
        }
        if (null == inventoryOut.getRowSts()) {
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
        }
        inventoryOutService.saveToInventoryOut(inventoryOut);
        Result<Object> result = Result.ok();
        result.setResult(inventoryOut);
        return result;
    }
    /**
     * 获取所有数据
     *
     * @param inventoryOut
     * @param req
     * @return
     */
    @ApiOperation(value = "获取出库单数据", notes = "获取所有出库单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(InventoryOut inventoryOut, HttpServletRequest req) {
        QueryWrapper<InventoryOut> queryWrapper = QueryGenerator.initQueryWrapper(inventoryOut, req.getParameterMap());
        List<InventoryOut> list = inventoryOutService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param inventoryOut
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取出库单数据列表", notes = "获取所有出库单数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(InventoryOut inventoryOut, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<InventoryOut> queryWrapper = QueryGenerator.initQueryWrapper(inventoryOut, req.getParameterMap());
        Page<InventoryOut> page = new Page<InventoryOut>(pageNo, pageSize);
        queryWrapper.eq("row_sts", RowSts.EFFECTIVE.getId());
        IPage<InventoryOut> pageList = inventoryOutService.page(page, queryWrapper);
        List<InventoryOut> inventoryOutList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(inventoryOutList)) {
            List<String> sourceCodes = inventoryOutList.stream().map(InventoryOut::getSourceCode).collect(Collectors.toList());
            LambdaQueryWrapper<ReceiptOrder> receiptOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
            receiptOrderLambdaQueryWrapper.in(ReceiptOrder::getSourceBillCode, sourceCodes);
            receiptOrderLambdaQueryWrapper.eq(ReceiptOrder::getCompanyId, LoginUtils.getLoginUser().getCompanyId());
            List<ReceiptOrder> receiptOrders = receiptOrderService.list(receiptOrderLambdaQueryWrapper);
            Map<String, Integer> receiptMap = receiptOrders.stream().collect(Collectors.toMap(ReceiptOrder::getSourceBillCode, ReceiptOrder::getBillStatusId));
            inventoryOutList.stream().forEach(o->{
                o.setBillTypeName(EnumConvertUtils.getName(BillType.class, o.getBillType()));
                o.setSourceBillTypeName(EnumConvertUtils.getName(BillType.class, o.getSourceBillType()));
                o.setBillStatusName(EnumConvertUtils.getName(BillStatus.class, o.getBillStatus()));
                o.setReceiptStatus(EnumConvertUtils.getName(BillStatus.class, receiptMap.get(o.getSourceCode())));
            });
            pageList.setRecords(inventoryOutList);
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
     * @param inventoryOut
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改出库单")
    @ApiOperation(value = "修改出库单", notes = "修改出库单")
    public Result<?> edit(@RequestBody InventoryOut inventoryOut){
        inventoryOutService.updateById(inventoryOut);
        Result<Object> result = Result.ok();
        result.setResult(inventoryOut);
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除出库单")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除出库单", notes = "通过ID删除出库单")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        inventoryOutService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除出库单", notes = "批量删除出库单")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.inventoryOutService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询出库单", notes = "通过ID查询出库单")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        InventoryOut inventoryOut = inventoryOutService.getById(id);
        return Result.ok(inventoryOut);
    }

    /**
     * 获取待发货产品列表
     *
     * @param id
     * @param sourceId
     * @return
     */
    @ApiOperation(value = "获取销售订单待发货产品列表", notes = "获取销售订单待发货产品列表")
    @GetMapping(value = "/mtl/getList")
    public Result<?> getList(@ApiParam(name = "id", value = "出库单信息ID", required = true) @RequestParam(name = "id", required = true) String id,
                             @ApiParam(name = "sourceId", value = "原单id", required = true) @RequestParam(name = "sourceId", required = true) String sourceId) {
        List<PreInventoryOutMtl> list = inventoryOutService.getDeliveryMtlList(id, sourceId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> mtlIds = list.stream().map(PreInventoryOutMtl::getMtlId).collect(Collectors.toList());
            List<String> unitIds = list.stream().map(PreInventoryOutMtl::getUnitId).collect(Collectors.toList());
            List<String> auxiliaryIds = list.stream().map(PreInventoryOutMtl::getAuxiliaryId).collect(Collectors.toList());
            Collection<Material> materials = materialService.listByIds(mtlIds);
            Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
            Collection<MaterialAuxiliary> auxiliaries = materialAuxiliaryService.listByIds(auxiliaryIds);
            Map<String, String> mtlNameMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
            Map<String, String> mtlCodeMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getCode));
            Map<String, String> mtlSpectxMap = materials.stream().filter(o->StringUtils.isNotBlank(o.getSpecification())).collect(Collectors.toMap(Material::getId, Material::getSpecification));
            Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
            Map<String, String> auxiliaryMap = auxiliaries.stream().collect(Collectors.toMap(MaterialAuxiliary::getId, MaterialAuxiliary::getSuppValueMap));
            list.stream().forEach(o->{
                o.setUnit(unitMap.get(o.getUnitId()));
                o.setMtl(mtlNameMap.get(o.getMtlId()));
                o.setMtlCode(mtlCodeMap.get(o.getMtlId()));
                o.setSpecification(mtlSpectxMap.get(o.getMtlId()));
                o.setSuppValueMap(auxiliaryMap.get(o.getAuxiliaryId()));
            });
        }
        return Result.ok(list);
    }

    /**
     * 修改
     *
     * @param preInventoryDto
     * @return
     */
    @PostMapping(value = "/mtls/stockout")
    @AutoLog(value = "修改销售订单发货信息")
    @ApiOperation(value = "修改销售订单发货信息", notes = "修改销售订单发货信息")
    public Result<?> stockout(@RequestBody PreInventoryDto preInventoryDto){
        if (null != preInventoryDto && CollectionUtils.isNotEmpty(preInventoryDto.getMtls())) {
            inventoryOutService.stockOut(preInventoryDto);
        }
        return Result.ok();
    }

}
