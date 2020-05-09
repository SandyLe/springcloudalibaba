package org.jeecg.modules.repair.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.service.CustomerService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.repair.entity.RepairOrder;
import org.jeecg.modules.repair.service.RepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "工单列表")
@RestController
@RequestMapping("/repairOrder")
public class RepairOrderController extends JeecgController<RepairOrder, RepairOrderService> {
    @Autowired
    private RepairOrderService repairOrderService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(RepairOrder repairOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<RepairOrder> queryWrapper = QueryGenerator.initQueryWrapper(repairOrder, req.getParameterMap());
        Page<RepairOrder> page = new Page<>(pageNo, pageSize);
        IPage<RepairOrder> pageList = repairOrderService.page(page, queryWrapper);
        List<RepairOrder> datas = pageList.getRecords();
        List<String> customerIds = datas.stream().map(RepairOrder::getCustomerId).collect(Collectors.toList());
        Collection<Customer> customers = customerService.listByIds(customerIds);
        Map<String, String> customerMap = customers.stream().collect(Collectors.toMap(Customer::getId, Customer::getName));
        datas.stream().forEach(o->{
            o.setCustomer(customerMap.get(o.getCustomerId()));
        });
        pageList.setRecords(datas);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody RepairOrder repairOrder) {
        repairOrderService.saveOrder(repairOrder);
        return Result.ok();
    }

    @PostMapping("/edit")
    public Result<?> edit(@RequestBody RepairOrder repairOrder) {
        repairOrderService.editOrder(repairOrder);
        return Result.ok();
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        repairOrderService.removeById(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        repairOrderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/getOne")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        RepairOrder repairOrder = repairOrderService.getById(id);
        if (repairOrder == null) {
            return Result.ok("未找到对应数据");
        }
        return Result.ok(repairOrder);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code) {

        LambdaQueryWrapper<RepairOrder> lambdaQueryWrapper = new LambdaQueryWrapper<RepairOrder>().eq(RepairOrder::getCode, code);
        RepairOrder repairOrder = repairOrderService.getOne(lambdaQueryWrapper);
        if (repairOrder == null) {
            return Result.ok("未找到对应数据");
        }
        return Result.ok(repairOrder);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RepairOrder repairOrder) {
        return super.exportXls(request, repairOrder, RepairOrder.class, "拆卸单列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, RepairOrder.class);
    }
}
