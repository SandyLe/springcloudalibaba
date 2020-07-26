package org.jeecg.modules.invoice.controller;

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
import org.jeecg.common.enums.AddressType;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Area;
import org.jeecg.modules.basic.service.AreaService;
import org.jeecg.modules.invoice.entity.InvoiceAddress;
import org.jeecg.modules.invoice.service.InvoiceAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "销售订单地址")
@RestController
@RequestMapping("/invoiceAddress")
public class InvoiceAddressController {

    @Autowired
    private InvoiceAddressService invoiceAddressService;
    @Autowired
    private AreaService areaService;

    /**
     * 分页列表查询
     *
     * @param invoiceAddress
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取地址数据列表", notes = "获取所有地址数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(InvoiceAddress invoiceAddress, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<InvoiceAddress> queryWrapper = QueryGenerator.initQueryWrapper(invoiceAddress, req.getParameterMap());
        Page<InvoiceAddress> page = new Page<InvoiceAddress>(pageNo, pageSize);
        IPage<InvoiceAddress> pageList = invoiceAddressService.page(page, queryWrapper);
        List<InvoiceAddress> data = pageList.getRecords();
        data.forEach(address->{
            address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
            address.setFullAddress(getFullinvoiceAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
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
    @ApiOperation(value = "获取客户来源数据", notes = "获取所有客户来源数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(InvoiceAddress address, HttpServletRequest req) {
        QueryWrapper<InvoiceAddress> queryWrapper = QueryGenerator.initQueryWrapper(address, req.getParameterMap());
        List<InvoiceAddress> list = invoiceAddressService.list(queryWrapper);
        list.forEach(o -> {
            o.setTypeName(EnumConvertUtils.getName(AddressType.class, o.getTypeId()));
            o.setFullAddress(getFullinvoiceAddress(o.getProvince(), o.getCity(), o.getDistrict(), o.getName()));
        });
        return Result.ok(list);
    }
    /**
     * 保存
     *
     * @param invoiceAddress
     * @return
     */
    @PostMapping(value = "/save")
        @AutoLog(value = "保存地址")
    @ApiOperation(value = "保存地址", notes = "保存地址")
    public Result<?> add(@RequestBody InvoiceAddress invoiceAddress) {

        InvoiceAddress dbinvoiceAddress = invoiceAddressService.findBySouorceId(invoiceAddress.getSourceId());
        if (null != dbinvoiceAddress) {
            dbinvoiceAddress.setTypeId(invoiceAddress.getTypeId());
            dbinvoiceAddress.setCity(invoiceAddress.getCity());
            dbinvoiceAddress.setProvince(invoiceAddress.getProvince());
            dbinvoiceAddress.setDistrict(invoiceAddress.getDistrict());
            dbinvoiceAddress.setRecipients(invoiceAddress.getRecipients());
            dbinvoiceAddress.setSourceAddId(invoiceAddress.getSourceAddId());
            dbinvoiceAddress.setTel(invoiceAddress.getTel());
            dbinvoiceAddress.setName(invoiceAddress.getName());
            invoiceAddressService.saveOrUpdate(dbinvoiceAddress);
        } else {
            invoiceAddressService.save(invoiceAddress);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 修改
     *
     * @param invoiceAddress
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改地址")
    @ApiOperation(value = "修改地址", notes = "修改地址")
    public Result<?> edit(@RequestBody InvoiceAddress invoiceAddress){
        invoiceAddressService.updateById(invoiceAddress);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除地址")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除地址", notes = "通过ID删除地址")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        invoiceAddressService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除地址", notes = "批量删除地址")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.invoiceAddressService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询地址", notes = "通过ID查询地址")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        InvoiceAddress address = invoiceAddressService.getById(id);
        address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
        address.setFullAddress(getFullinvoiceAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
        return Result.ok(address);
    }

    /**
     * 通过sourceId查询
     *
     * @param sourceId
     * @return
     */
    @GetMapping(value = "/getOneBySourceId")
    @ApiOperation(value = "通过ID查询地址", notes = "通过ID查询地址")
    public Result<?> getBySourceId(@ApiParam(name = "sourceId", value = "原单Id", required = true) @RequestParam(name = "sourceId", required = true) String sourceId) {
        InvoiceAddress address = invoiceAddressService.findBySouorceId(sourceId);
        address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
        address.setFullAddress(getFullinvoiceAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
        return Result.ok(address);
    }


    private String getFullinvoiceAddress(String provinceId, String cityId, String districtId, String address) {

        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(provinceId)) {
            Area province = areaService.getById(provinceId);
            stringBuilder.append(null != province ? province.getName() : "");
        }
        if (StringUtils.isNotBlank(cityId)) {
            Area city = areaService.getById(cityId);
            stringBuilder.append(null != city ? city.getName() : "");
        }
        if (StringUtils.isNotBlank(districtId)) {
            Area district = areaService.getById(districtId);
            stringBuilder.append(null != district ? district.getName() : "");
        }
        if (StringUtils.isNotBlank(address)) {
            stringBuilder.append(address);
        }
        return stringBuilder.toString();
    }
}
