package org.jeecg.modules.basic.controller;

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
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.entity.*;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.*;
import org.jeecg.modules.system.service.ISysDictService;
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
@Api(tags = "客户")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerSourceService customerSourceService;
    @Autowired
    private CustomerTypeService customerTypeService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private AddressService addressService;

    /**
     * 获取所有数据
     *
     * @param customer
     * @param req
     * @return
     */
    @ApiOperation(value = "获取客户数据", notes = "获取所有客户数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(Customer customer, HttpServletRequest req) {
        QueryWrapper<Customer> queryWrapper = QueryGenerator.initQueryWrapper(customer, req.getParameterMap());
        List<Customer> list = customerService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param customer
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取客户数据列表", notes = "获取所有客户数据列表")
    @GetMapping(value = "/getPage")
    @PermissionData
    public Result<?> list(Customer customer, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Customer> queryWrapper = QueryGenerator.initQueryWrapper(customer, req.getParameterMap());
        Page<Customer> page = new Page<Customer>(pageNo, pageSize);

        IPage<Customer> pageList = customerService.page(page, queryWrapper);
        List<Customer> customerList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(customerList)) {
            List<String> customerTypeIds = customerList.stream().map(Customer::getCustomerTypeId).collect(Collectors.toList());
            List<String> customerSourceIds = customerList.stream().map(Customer::getCustomerSourceId).collect(Collectors.toList());
            Collection<CustomerType> customerTypes = customerTypeService.listByIds(customerTypeIds);
            Collection<CustomerSource> customerSources = customerSourceService.listByIds(customerSourceIds);
            Map<String, String> customerTypeMap = customerTypes.stream().collect(Collectors.toMap(CustomerType::getId, CustomerType::getName));
            Map<String, String> customerSourceMap = customerSources.stream().collect(Collectors.toMap(CustomerSource:: getId, CustomerSource:: getName));
            customerList.stream().forEach(o->{
                o.setCustomerSource(customerSourceMap.get(o.getCustomerSourceId()));
                o.setCustomerType(customerTypeMap.get(o.getCustomerTypeId()));
            });
        }

        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }
    /**
     * 添加
     *
     * @param customer
     * @return
     */
    @PostMapping(value = "/save")
    @AutoLog(value = "添加客户")
    @ApiOperation(value = "添加客户", notes = "添加客户")
    public Result<?> add(@RequestBody Customer customer) throws Exception {
        LoginUser sysUser = LoginUtils.getLoginUser();
        if (StringUtils.isBlank(customer.getCompanyId())) {
            customer.setCompanyId(sysUser.getCompanyId());
        }
        if (null != customer.getRowSts()) {
            customer.setRowSts(RowSts.EFFECTIVE.getId());
        }
        Customer exists = null;
        if (StringUtils.isEmpty(customer.getId())) {
            customer.setCode(billCodeBuilderService.getBillCode(BillType.CUSTOMER.getId()));
                LambdaQueryWrapper<Customer> lambdaQueryWrapper = new LambdaQueryWrapper<Customer>().eq(Customer::getCode, customer.getCode());
            lambdaQueryWrapper.eq(Customer::getCompanyId, customer.getCompanyId());
            exists = customerService.getOne(lambdaQueryWrapper);
        } else {
            customer.setCode(customer.getCode());
            LambdaQueryWrapper<Customer> lambdaQueryWrapper = new LambdaQueryWrapper<Customer>().eq(Customer::getCode, customer.getCode()).ne(Customer::getId, customer.getId());
            lambdaQueryWrapper.eq(Customer::getCompanyId, customer.getCompanyId());
            exists = customerService.getOne(lambdaQueryWrapper);
        }
        Assert.isNull(exists, "编号已存在！");
        customerService.saveOrUpdate(customer);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param customer
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改客户")
    @ApiOperation(value = "修改客户", notes = "修改客户")
    public Result<?> edit(@RequestBody Customer customer){
        customerService.updateById(customer);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除客户")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除客户", notes = "通过ID删除客户")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        customerService.removeById(id);
        addressService.remove(new QueryWrapper<Address>().lambda().eq(Address::getSourceId, id));
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除客户", notes = "批量删除客户")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.customerService.removeByIds(Arrays.asList(ids.split(",")));
        addressService.remove(new LambdaQueryWrapper<Address>().in(Address::getSourceId, Arrays.asList(ids.split(","))));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询客户", notes = "通过ID查询客户")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        Customer customer = customerService.getById(id);
        CustomerSource source = customerSourceService.getById(customer.getCustomerSourceId());
        CustomerType type = customerTypeService.getById(customer.getCustomerTypeId());
        customer.setCustomerSource(null != source ? source.getName() : null);
        customer.setCustomerType(null != type ? type.getName() : null);
        customer.setRowStsName(EnumConvertUtils.getName(RowSts.class, customer.getRowSts()));
        if (null != customer.getGender()) {
            customer.setGenderName(iSysDictService.queryDictTextByKey("sex", customer.getGender()));
        }
        if (StringUtils.isNotBlank(customer.getDiscountTypeId())) {
            customer.setDiscountType(iSysDictService.queryDictTextByKey("discount_type", customer.getDiscountTypeId()));
        }
        return Result.ok(customer);
    }

    /**
     * 搜索客户
     *
     * @param keyword
     * @return
     */
    @ApiOperation(value = "搜索", notes = "搜索")
    @GetMapping(value = "/search")
    @PermissionData
    public Result<?> search(Customer customer,
                            @ApiParam(name = "keyword", value = "关键词", required = true) @RequestParam(name = "keyword", required = false) String keyword,
                            HttpServletRequest req) {
        QueryWrapper<Customer> queryWrapper = QueryGenerator.initQueryWrapper(customer, req.getParameterMap());
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like("name", keyword).or().like("code", keyword).or().like("phone", keyword);
        }
        queryWrapper.orderByDesc("create_time").last("limit 0,20");
        List<Customer> list = customerService.list(queryWrapper);
        return Result.ok(list);
    }
}
