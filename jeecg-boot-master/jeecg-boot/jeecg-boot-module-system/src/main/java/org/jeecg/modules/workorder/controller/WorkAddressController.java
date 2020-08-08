package org.jeecg.modules.workorder.controller;

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
import org.jeecg.common.enums.AddressType;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.common.enums.WorkType;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Area;
import org.jeecg.modules.basic.service.AreaService;
import org.jeecg.modules.workorder.entity.WorkAddress;
import org.jeecg.modules.workorder.entity.WorkOrder;
import org.jeecg.modules.workorder.service.WorkAddressService;
import org.jeecg.modules.workorder.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "工单地址")
@RestController
@RequestMapping("/workAddress")
public class WorkAddressController {

    @Autowired
    private WorkAddressService workAddressService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private AreaService areaService;

    /**
     * 分页列表查询
     *
     * @param workAddress
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取地址数据列表", notes = "获取所有地址数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(WorkAddress workAddress, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<WorkAddress> queryWrapper = QueryGenerator.initQueryWrapper(workAddress, req.getParameterMap());
        Page<WorkAddress> page = new Page<WorkAddress>(pageNo, pageSize);
        IPage<WorkAddress> pageList = workAddressService.page(page, queryWrapper);
        List<WorkAddress> data = pageList.getRecords();
        data.forEach(address->{
            address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
            address.setFullAddress(getFullworkAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
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
    public Result<?> getList(WorkAddress address, HttpServletRequest req) {
        QueryWrapper<WorkAddress> queryWrapper = QueryGenerator.initQueryWrapper(address, req.getParameterMap());
        List<WorkAddress> list = workAddressService.list(queryWrapper);
        list.forEach(o -> {
            o.setTypeName(EnumConvertUtils.getName(AddressType.class, o.getTypeId()));
            o.setFullAddress(getFullworkAddress(o.getProvince(), o.getCity(), o.getDistrict(), o.getName()));
        });
        return Result.ok(list);
    }
    /**
     * 保存
     *
     * @param workAddress
     * @return
     */
    @PostMapping(value = "/save")
        @AutoLog(value = "保存地址")
    @ApiOperation(value = "保存地址", notes = "保存地址")
    public Result<?> add(@RequestBody WorkAddress workAddress) {

        WorkAddress dbworkAddress = workAddressService.findBySouorceId(workAddress.getSourceId());
        if (null != dbworkAddress) {
            dbworkAddress.setTypeId(workAddress.getTypeId());
            dbworkAddress.setCity(workAddress.getCity());
            dbworkAddress.setProvince(workAddress.getProvince());
            dbworkAddress.setDistrict(workAddress.getDistrict());
            dbworkAddress.setRecipients(workAddress.getRecipients());
            dbworkAddress.setSourceAddId(workAddress.getSourceAddId());
            dbworkAddress.setTel(workAddress.getTel());
            dbworkAddress.setName(workAddress.getName());
            workAddressService.saveOrUpdate(dbworkAddress);
        } else {
            workAddressService.save(workAddress);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 修改
     *
     * @param workAddress
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改地址")
    @ApiOperation(value = "修改地址", notes = "修改地址")
    public Result<?> edit(@RequestBody WorkAddress workAddress){
        workAddressService.updateById(workAddress);
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
        workAddressService.removeById(id);
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
        this.workAddressService.removeByIds(Arrays.asList(ids.split(",")));
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
        WorkAddress address = workAddressService.getById(id);
        address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
        address.setFullAddress(getFullworkAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
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
        WorkAddress address = workAddressService.findBySouorceId(sourceId);
        address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
        address.setFullAddress(getFullworkAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
        return Result.ok(address);
    }

    /**
     * 根据销售订单ID获取安装地址
     * @param saleId
     * @return
     */
    @GetMapping(value = "/getInstallAddress")
    @ApiOperation(value = "通过ID查询地址", notes = "通过ID查询地址")
    public Result<?> getInstallAddress(@ApiParam(name = "saleId", value = "原单销售Id", required = true) @RequestParam(name = "saleId", required = true) String saleId) {


        WorkAddress address = new WorkAddress();
        List<WorkOrder> workOrders = workOrderService.findBySourceId(saleId, WorkType.Install.getId());
        if (CollectionUtils.isNotEmpty(workOrders)) {
            address = workAddressService.findBySouorceId(workOrders.get(0).getId());
            address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
            address.setFullAddress(getFullworkAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
        }
        return Result.ok(address);
    }


    private String getFullworkAddress(String provinceId, String cityId, String districtId, String address) {

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
