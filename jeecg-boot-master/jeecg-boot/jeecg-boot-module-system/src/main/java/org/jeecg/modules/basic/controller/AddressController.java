package org.jeecg.modules.basic.controller;

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
import org.jeecg.modules.basic.entity.Address;
import org.jeecg.modules.basic.entity.Area;
import org.jeecg.modules.basic.service.AddressService;
import org.jeecg.modules.basic.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "地区")
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private AreaService areaService;

    /**
     * 分页列表查询
     *
     * @param Address
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取地区数据列表", notes = "获取所有地区数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(Address Address, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Address> queryWrapper = QueryGenerator.initQueryWrapper(Address, req.getParameterMap());
        Page<Address> page = new Page<Address>(pageNo, pageSize);
        IPage<Address> pageList = addressService.page(page, queryWrapper);
        List<Address> data = pageList.getRecords();
        data.forEach(address->{
            address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
            address.setFullAddress(getFullAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
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
    public Result<?> getList(Address address, HttpServletRequest req) {
        QueryWrapper<Address> queryWrapper = QueryGenerator.initQueryWrapper(address, req.getParameterMap());
        List<Address> list = addressService.list(queryWrapper);
        list.forEach(o -> {
            o.setTypeName(EnumConvertUtils.getName(AddressType.class, o.getTypeId()));
            o.setFullAddress(getFullAddress(o.getProvince(), o.getCity(), o.getDistrict(), o.getName()));
        });
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param Address
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加地区")
    @ApiOperation(value = "添加地区", notes = "添加地区")
    public Result<?> add(@RequestBody Address Address) {
        addressService.save(Address);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param Address
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改地区")
    @ApiOperation(value = "修改地区", notes = "修改地区")
    public Result<?> edit(@RequestBody Address Address){
        addressService.updateById(Address);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除地区")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除地区", notes = "通过ID删除地区")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        addressService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除地区", notes = "批量删除地区")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.addressService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询地区", notes = "通过ID查询地区")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        Address address = addressService.getById(id);
        address.setTypeName(EnumConvertUtils.getName(AddressType.class, address.getTypeId()));
        address.setFullAddress(getFullAddress(address.getProvince(), address.getCity(), address.getDistrict(), address.getName()));
        return Result.ok(address);
    }


    private String getFullAddress(String provinceId, String cityId, String districtId, String address) {

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
