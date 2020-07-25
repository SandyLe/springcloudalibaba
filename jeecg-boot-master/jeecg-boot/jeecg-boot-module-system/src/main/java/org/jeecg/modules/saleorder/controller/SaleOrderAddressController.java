package org.jeecg.modules.saleorder.controller;

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
import org.jeecg.modules.saleorder.entity.SaleOrderAddress;
import org.jeecg.modules.saleorder.service.SaleOrderAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "销售订单地址")
@RestController
@RequestMapping("/saleOrderAddress")
public class SaleOrderAddressController {

    @Autowired
    private SaleOrderAddressService saleOrderAddressService;
    @Autowired
    private AreaService areaService;

    /**
     * 分页列表查询
     *
     * @param saleOrderAddress
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取地址数据列表", notes = "获取所有地址数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(SaleOrderAddress saleOrderAddress, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SaleOrderAddress> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderAddress, req.getParameterMap());
        Page<SaleOrderAddress> page = new Page<SaleOrderAddress>(pageNo, pageSize);
        IPage<SaleOrderAddress> pageList = saleOrderAddressService.page(page, queryWrapper);
        List<SaleOrderAddress> data = pageList.getRecords();
        data.forEach(address->{
            address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
            address.setFullAddress(getFullSaleOrderAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
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
    public Result<?> getList(SaleOrderAddress address, HttpServletRequest req) {
        QueryWrapper<SaleOrderAddress> queryWrapper = QueryGenerator.initQueryWrapper(address, req.getParameterMap());
        List<SaleOrderAddress> list = saleOrderAddressService.list(queryWrapper);
        list.forEach(o -> {
            o.setTypeName(EnumConvertUtils.getName(AddressType.class, o.getTypeId()));
            o.setFullAddress(getFullSaleOrderAddress(o.getProvince(), o.getCity(), o.getDistrict(), o.getName()));
        });
        return Result.ok(list);
    }
    /**
     * 保存
     *
     * @param saleOrderAddress
     * @return
     */
    @PostMapping(value = "/save")
        @AutoLog(value = "保存地址")
    @ApiOperation(value = "保存地址", notes = "保存地址")
    public Result<?> add(@RequestBody SaleOrderAddress saleOrderAddress) {

        SaleOrderAddress dbSaleOrderAddress = saleOrderAddressService.findBySouorceId(saleOrderAddress.getSourceId());
        if (null != dbSaleOrderAddress) {
            dbSaleOrderAddress.setTypeId(saleOrderAddress.getTypeId());
            dbSaleOrderAddress.setCity(saleOrderAddress.getCity());
            dbSaleOrderAddress.setProvince(saleOrderAddress.getProvince());
            dbSaleOrderAddress.setDistrict(saleOrderAddress.getDistrict());
            dbSaleOrderAddress.setRecipients(saleOrderAddress.getRecipients());
            dbSaleOrderAddress.setSourceAddId(saleOrderAddress.getSourceAddId());
            dbSaleOrderAddress.setTel(saleOrderAddress.getTel());
            dbSaleOrderAddress.setName(saleOrderAddress.getName());
            saleOrderAddressService.saveOrUpdate(dbSaleOrderAddress);
        } else {
            saleOrderAddressService.save(saleOrderAddress);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 修改
     *
     * @param saleOrderAddress
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改地址")
    @ApiOperation(value = "修改地址", notes = "修改地址")
    public Result<?> edit(@RequestBody SaleOrderAddress saleOrderAddress){
        saleOrderAddressService.updateById(saleOrderAddress);
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
        saleOrderAddressService.removeById(id);
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
        this.saleOrderAddressService.removeByIds(Arrays.asList(ids.split(",")));
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
        SaleOrderAddress address = saleOrderAddressService.getById(id);
        address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
        address.setFullAddress(getFullSaleOrderAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
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
        SaleOrderAddress address = saleOrderAddressService.findBySouorceId(sourceId);
        address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
        address.setFullAddress(getFullSaleOrderAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
        return Result.ok(address);
    }


    private String getFullSaleOrderAddress(String provinceId, String cityId, String districtId, String address) {

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
