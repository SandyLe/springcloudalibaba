package org.jeecg.modules.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.entity.LogisticsCompany;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.service.CustomerService;
import org.jeecg.modules.basic.service.LogisticsCompanyService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.logistics.dto.LogisticsOrderDto;
import org.jeecg.modules.logistics.entity.LogisticsOrder;
import org.jeecg.modules.logistics.service.LogisticsOrderDtlService;
import org.jeecg.modules.logistics.service.LogisticsOrderService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "物流单列表")
@RestController
@RequestMapping("/logisticsOrder")
public class LogisticsOrderController extends JeecgController<LogisticsOrder, LogisticsOrderService> {
    @Autowired
    private LogisticsOrderService logisticsOrderService;
    @Autowired
    private LogisticsOrderDtlService logisticsOrderDtlService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private LogisticsCompanyService logisticsCompanyService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(LogisticsOrder logisticsOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<LogisticsOrder> queryWrapper = QueryGenerator.initQueryWrapper(logisticsOrder, req.getParameterMap());
        Page<LogisticsOrder> page = new Page<>(pageNo, pageSize);
        IPage<LogisticsOrder> pageList = logisticsOrderService.page(page, queryWrapper);
        List<LogisticsOrder> datas = pageList.getRecords();
        List<String> logisticsIds = datas.stream().map(LogisticsOrder::getCdiLogisticsId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(logisticsIds)) {
            Collection<LogisticsCompany> companies = logisticsCompanyService.listByIds(logisticsIds);
            Map<String, String> idNameMap = companies.stream().collect(Collectors.toMap(LogisticsCompany::getId, LogisticsCompany::getName));
            datas.stream().forEach(o->{o.setCdiLogistics(idNameMap.get(o.getCdiLogisticsId()));});
        }
        pageList.setRecords(datas);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody LogisticsOrderDto logisticsOrderdto) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(logisticsOrderdto.getCompanyId())) {
            logisticsOrderdto.setCompanyId(sysUser.getCompanyId());
        }
        logisticsOrderdto.setBillType(BillType.LOGISTICSORDER.getId());
        logisticsOrderService.saveOrder(logisticsOrderdto);
        return Result.ok();
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody LogisticsOrderDto logisticsOrderdto) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(logisticsOrderdto.getCompanyId())) {
            logisticsOrderdto.setCompanyId(sysUser.getCompanyId());
        }
        logisticsOrderService.editOrder(logisticsOrderdto);
        return Result.ok();
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        logisticsOrderService.removeById(id);
        logisticsOrderDtlService.deleteBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        logisticsOrderService.removeByIds(Arrays.asList(ids.split(",")));
        logisticsOrderDtlService.deleteBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/getOne")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        LogisticsOrder logisticsOrder = logisticsOrderService.getById(id);
        if (logisticsOrder == null) {
            return Result.ok("未找到对应数据");
        }
        LogisticsOrderDto logisticsOrderdtldto = new LogisticsOrderDto();
        BeanUtils.copyProperties(logisticsOrder, logisticsOrderdtldto);
        logisticsOrderdtldto.setDetaillist(logisticsOrderDtlService.findBySourceId(logisticsOrder.getId()));

        return Result.ok(logisticsOrderdtldto);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code) {

        LambdaQueryWrapper<LogisticsOrder> lambdaQueryWrapper = new LambdaQueryWrapper<LogisticsOrder>().eq(LogisticsOrder::getCode, code);
        LogisticsOrder logisticsOrder = logisticsOrderService.getOne(lambdaQueryWrapper);
        if (logisticsOrder == null) {
            return Result.ok("未找到对应数据");
        }
        return Result.ok(logisticsOrder);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogisticsOrder logisticsOrder) {
        return super.exportXls(request, logisticsOrder, LogisticsOrder.class, "拆卸单列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, LogisticsOrder.class);
    }
}
