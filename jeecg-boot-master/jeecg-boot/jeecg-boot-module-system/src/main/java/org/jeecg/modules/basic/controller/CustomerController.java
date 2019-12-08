package org.jeecg.modules.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.dto.CustomerEditDto;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.entity.CustomerDeliveryInfo;
import org.jeecg.modules.basic.service.CustomerDeliveryInfoService;
import org.jeecg.modules.basic.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Api(tags = "客户")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerDeliveryInfoService customerDeliveryInfoService;

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
    public Result<?> list(Customer customer, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Customer> queryWrapper = QueryGenerator.initQueryWrapper(customer, req.getParameterMap());
        Page<Customer> page = new Page<Customer>(pageNo, pageSize);

        IPage<Customer> pageList = customerService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }
    /**
     * 添加
     *
     * @param customerEditDto
     * @return
     */
    @PostMapping(value = "/save")
    @AutoLog(value = "添加客户")
    @ApiOperation(value = "添加客户", notes = "添加客户")
    public Result<?> add(@RequestBody CustomerEditDto customerEditDto) throws Exception {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customer, customerEditDto);
        customer.setId(customerEditDto.getId());
        customer.setCode(customerEditDto.getCode());
        customerService.saveOrUpdate(customer);
        CustomerDeliveryInfo cdi = new CustomerDeliveryInfo();
        BeanUtils.copyProperties(cdi,customerEditDto);
        cdi.setCdiSourceId(customer.getId());
        cdi.setId(customerEditDto.getCdiId());
        cdi.setCreateBy(customerEditDto.getCdiCreateBy());
        cdi.setCreateTime(customerEditDto.getCdiCreateTime());
        cdi.setUpdateBy(customerEditDto.getCdiUpdateBy());
        cdi.setUpdateTime(customerEditDto.getCdiUpdateTime());
        cdi.setCode(customerEditDto.getCdiCode());
        cdi.setName(customerEditDto.getCdiName());
        cdi.setRowSts(customerEditDto.getCdiRowSts());
        cdi.setSort(customerEditDto.getCdiSort());
        cdi.setCode(customerEditDto.getCdiContent());
        customerDeliveryInfoService.saveOrUpdate(cdi);
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
        customerDeliveryInfoService.remove(new QueryWrapper<CustomerDeliveryInfo>().lambda().eq(CustomerDeliveryInfo::getCdiSourceId, id));
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
        customerDeliveryInfoService.remove(new LambdaQueryWrapper<CustomerDeliveryInfo>().in(CustomerDeliveryInfo::getCdiSourceId, Arrays.asList(ids.split(","))));
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
        return Result.ok(customer);
    }

    @GetMapping(value = "/getDeliveryInfo")
    @ApiModelProperty(value = "查询收货信息", notes = "查询收货信息")
    public Result<?> getDeliveryInfo(CustomerDeliveryInfo info, HttpServletRequest req){
        CustomerDeliveryInfo result = customerDeliveryInfoService.getOne(QueryGenerator.initQueryWrapper(info, req.getParameterMap()));
        return Result.ok(result);
    }
}
