package org.jeecg.modules.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.common.enums.RowSts;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.*;
import org.jeecg.modules.basic.service.*;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "服务机构")
@RestController
@RequestMapping("/serviceInstitution")
public class ServiceInstitutionController {

    @Autowired
    private ServiceInstitutionService serviceInstitutionService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private AreaService areaService;

    /**
     * 获取所有数据
     *
     * @param serviceInstitution
     * @param req
     * @return
     */
    @ApiOperation(value = "获取服务机构数据", notes = "获取所有服务机构数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(ServiceInstitution serviceInstitution, HttpServletRequest req) {
        QueryWrapper<ServiceInstitution> queryWrapper = QueryGenerator.initQueryWrapper(serviceInstitution, req.getParameterMap());
        List<ServiceInstitution> list = serviceInstitutionService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param serviceInstitution
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取服务机构数据列表", notes = "获取所有服务机构数据列表")
    @GetMapping(value = "/getPage")
    @PermissionData
    public Result<?> list(ServiceInstitution serviceInstitution, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<ServiceInstitution> queryWrapper = QueryGenerator.initQueryWrapper(serviceInstitution, req.getParameterMap());
        Page<ServiceInstitution> page = new Page<ServiceInstitution>(pageNo, pageSize);
        IPage<ServiceInstitution> pageList = serviceInstitutionService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }
    /**
     * 添加
     *
     * @param institution
     * @return
     */
    @PostMapping(value = "/save")
    @AutoLog(value = "添加服务机构")
    @ApiOperation(value = "添加服务机构", notes = "添加服务机构")
    public Result<?> add(@RequestBody ServiceInstitution institution) throws Exception {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(institution.getCompanyId())) {
            institution.setCompanyId(sysUser.getCompanyId());
        }
        ServiceInstitution serviceInstitution = new ServiceInstitution();
        BeanUtils.copyProperties(serviceInstitution, institution);
        serviceInstitution.setId(institution.getId());
        ServiceInstitution exists = null;
        if (StringUtils.isEmpty(institution.getId())) {
            serviceInstitution.setCode(billCodeBuilderService.getBillCode(BillType.SERVICEINSTITUTION.getId()));
                LambdaQueryWrapper<ServiceInstitution> lambdaQueryWrapper = new LambdaQueryWrapper<ServiceInstitution>().eq(ServiceInstitution::getCode, serviceInstitution.getCode());
            lambdaQueryWrapper.eq(ServiceInstitution::getCompanyId, institution.getCompanyId());
            exists = serviceInstitutionService.getOne(lambdaQueryWrapper);
        } else {
            serviceInstitution.setCode(institution.getCode());
            LambdaQueryWrapper<ServiceInstitution> lambdaQueryWrapper = new LambdaQueryWrapper<ServiceInstitution>().eq(ServiceInstitution::getCode, serviceInstitution.getCode()).ne(ServiceInstitution::getId, serviceInstitution.getId());
            lambdaQueryWrapper.eq(ServiceInstitution::getCompanyId, institution.getCompanyId());
            exists = serviceInstitutionService.getOne(lambdaQueryWrapper);
        }
        Assert.isNull(exists, "编号已存在！");
        serviceInstitutionService.saveOrUpdate(serviceInstitution);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param serviceInstitution
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改服务机构")
    @ApiOperation(value = "修改服务机构", notes = "修改服务机构")
    public Result<?> edit(@RequestBody ServiceInstitution serviceInstitution){
        serviceInstitutionService.updateById(serviceInstitution);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除服务机构")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除服务机构", notes = "通过ID删除服务机构")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        serviceInstitutionService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除服务机构", notes = "批量删除服务机构")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.serviceInstitutionService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询服务机构", notes = "通过ID查询服务机构")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        ServiceInstitution serviceInstitution = serviceInstitutionService.getById(id);
        serviceInstitution.setRowStsName(EnumConvertUtils.getName(RowSts.class, serviceInstitution.getRowSts()));
        serviceInstitution.setFullAddress(getFullAddress(serviceInstitution.getProvince(), serviceInstitution.getCity(), serviceInstitution.getDistrict(), serviceInstitution.getAddress()));
        return Result.ok(serviceInstitution);
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
    /**
     * 搜索服务机构
     *
     * @param keyword
     * @return
     */
    @ApiOperation(value = "搜索", notes = "搜索")
    @GetMapping(value = "/search")
    @PermissionData
    public Result<?> search(ServiceInstitution serviceInstitution,
                            @ApiParam(name = "keyword", value = "关键词", required = true) @RequestParam(name = "keyword", required = false) String keyword,
                            HttpServletRequest req) {
        QueryWrapper<ServiceInstitution> queryWrapper = QueryGenerator.initQueryWrapper(serviceInstitution, req.getParameterMap());
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like("name", keyword).or().like("code", keyword).or().like("phone", keyword);
        }
        queryWrapper.orderByDesc("create_time").last("limit 0,20");
        List<ServiceInstitution> list = serviceInstitutionService.list(queryWrapper);
        return Result.ok(list);
    }
}
