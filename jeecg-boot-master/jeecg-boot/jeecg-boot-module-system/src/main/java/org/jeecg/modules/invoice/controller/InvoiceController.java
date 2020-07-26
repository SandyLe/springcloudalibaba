package org.jeecg.modules.invoice.controller;

import cn.hutool.core.lang.ObjectId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.enums.*;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.invoice.entity.Invoice;
import org.jeecg.modules.invoice.service.InvoiceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Api(tags = "发票")
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * 分页列表查询
     *
     * @param invoice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取发票数据列表", notes = "获取所有发票数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(Invoice invoice, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Invoice> queryWrapper = QueryGenerator.initQueryWrapper(invoice, req.getParameterMap());
        Page<Invoice> page = new Page<Invoice>(pageNo, pageSize);
        IPage<Invoice> pageList = invoiceService.page(page, queryWrapper);
        List<Invoice> data = pageList.getRecords();
        data.forEach(i->{
            i.setInvoiceTextureName(InvoiceTexture.getName(i.getInvoiceTextureId()));
            i.setInvoiceTypeName(InvoiceType.getName(i.getInvoiceTypeId()));
            i.setSourceBillTypeName(BillType.getName(i.getSourceBillType()));
            i.setBillStatusName(InvoiceOrderStatus.getName(i.getBillStatusId()));
        });
        pageList.setRecords(data);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param address
     * @param req
     * @return
     */
    @ApiOperation(value = "获取发票数据", notes = "获取所有发票数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(Invoice address, HttpServletRequest req) {
        QueryWrapper<Invoice> queryWrapper = QueryGenerator.initQueryWrapper(address, req.getParameterMap());
        List<Invoice> list = invoiceService.list(queryWrapper);
        list.forEach(i -> {
            i.setInvoiceTextureName(InvoiceTexture.getName(i.getInvoiceTextureId()));
            i.setInvoiceTypeName(InvoiceType.getName(i.getInvoiceTypeId()));
            i.setSourceBillTypeName(BillType.getName(i.getSourceBillType()));
            i.setBillStatusName(InvoiceOrderStatus.getName(i.getBillStatusId()));
        });
        return Result.ok(list);
    }
    /**
     * 保存
     *
     * @param invoice
     * @return
     */
    @PostMapping(value = "/add")
        @AutoLog(value = "保存发票")
    @ApiOperation(value = "保存发票", notes = "保存发票")
    public Result<?> add(@RequestBody Invoice invoice) {

        if (StringUtils.isBlank(invoice.getCompanyId())) {
            invoice.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        }
        if (StringUtils.isBlank(invoice.getSalemanId())){
            invoice.setSalemanId(LoginUtils.getLoginUser().getId());
        }
        invoiceService.save(invoice);
        return Result.ok("保存成功！");
    }

    /**
     * 修改
     *
     * @param invoice
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改发票")
    @ApiOperation(value = "修改发票", notes = "修改发票")
    public Result<?> edit(@RequestBody Invoice invoice){
        invoiceService.updateById(invoice);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除发票")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除发票", notes = "通过ID删除发票")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        invoiceService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除发票", notes = "批量删除发票")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.invoiceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询发票", notes = "通过ID查询发票")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = false) @RequestParam(name = "id", required = false) String id) {
        Invoice invoice = null;
        if (StringUtils.isNotBlank(id)) {
            invoice = invoiceService.getById(id);
            invoice.setInvoiceTextureName(InvoiceTexture.getName(invoice.getInvoiceTextureId()));
            invoice.setInvoiceTypeName(InvoiceType.getName(invoice.getInvoiceTypeId()));
            invoice.setSourceBillTypeName(BillType.getName(invoice.getSourceBillType()));
            invoice.setBillStatusName(InvoiceOrderStatus.getName(invoice.getBillStatusId()));
        } else {
            invoice = new Invoice();
            invoice.setId(UUID.randomUUID().toString());
            invoice.setBillType(BillType.INVOICE.getId());
        }
        return Result.ok(invoice);
    }

    /**
     * 通过sourceId查询
     *
     * @param sourceId
     * @return
     */
    @GetMapping(value = "/getOneBySourceId")
    @ApiOperation(value = "通过ID查询发票", notes = "通过ID查询发票")
    public Result<?> getBySourceId(@ApiParam(name = "sourceId", value = "原单Id", required = true) @RequestParam(name = "sourceId", required = true) String sourceId) {
        Invoice invoice = invoiceService.findBySouorceId(sourceId);
        invoice.setInvoiceTextureName(InvoiceTexture.getName(invoice.getInvoiceTextureId()));
        invoice.setInvoiceTypeName(InvoiceType.getName(invoice.getInvoiceTypeId()));
        invoice.setSourceBillTypeName(BillType.getName(invoice.getSourceBillType()));
        invoice.setBillStatusName(InvoiceOrderStatus.getName(invoice.getBillStatusId()));
        return Result.ok(invoice);
    }


}
