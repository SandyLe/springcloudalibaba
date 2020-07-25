package org.jeecg.modules.workorder.controller;

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
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.WorkType;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.workorder.dto.WorkOrderDto;
import org.jeecg.modules.workorder.entity.WorkOrder;
import org.jeecg.modules.workorder.service.WorkOrderDtlService;
import org.jeecg.modules.workorder.service.WorkOrderService;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/workOrder")
public class WorkOrderController extends JeecgController<WorkOrder, WorkOrderService> {
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private WorkOrderDtlService workOrderDtlService;
    @Autowired
    private ISysUserService iSysUserService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(WorkOrder workOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Integer billStatus = workOrder.getBillStatus();
        if (null != billStatus) {
            workOrder.setBillStatus(null);
        }
        QueryWrapper<WorkOrder> queryWrapper = QueryGenerator.initQueryWrapper(workOrder, req.getParameterMap());
        if (billStatus == BillStatus.FINISHED.getId()) {
            queryWrapper.eq("bill_status", billStatus);
        } else {
            queryWrapper.ne("bill_status", BillStatus.FINISHED.getId());
        }
        Page<WorkOrder> page = new Page<>(pageNo, pageSize);
        IPage<WorkOrder> pageList = workOrderService.page(page, queryWrapper);
        List<WorkOrder> datas = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(datas)) {
            Map<String, String> userMap = new HashMap<>();

            List<String> userIds = datas.stream().map(WorkOrder::getOperateUserId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(userIds)) {
                Collection<SysUser> sysUsers = iSysUserService.listByIds(userIds);
                userMap.putAll(sysUsers.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getRealname)));
            }
            datas.stream().forEach(o -> {
                o.setOperateUserName(userMap.get(o.getOperateUserId()));
                o.setWorkTypeName(WorkType.getName(o.getWorkTypeId()));
            });
        }
        pageList.setRecords(datas);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody WorkOrderDto workOrderdto) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(workOrderdto.getCompanyId())) {
            workOrderdto.setCompanyId(sysUser.getCompanyId());
        }
        workOrderService.saveOrder(workOrderdto);
        return Result.ok();
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody WorkOrderDto workOrderdto) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(workOrderdto.getCompanyId())) {
            workOrderdto.setCompanyId(sysUser.getCompanyId());
        }
        workOrderService.editOrder(workOrderdto);
        return Result.ok();
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        workOrderService.removeById(id);
        workOrderDtlService.deleteBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        workOrderService.removeByIds(Arrays.asList(ids.split(",")));
        workOrderDtlService.deleteBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/getOne")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WorkOrder workOrder = workOrderService.getById(id);
        if (workOrder == null) {
            return Result.ok("未找到对应数据");
        }
        WorkOrderDto workOrderdtldto = new WorkOrderDto();
        BeanUtils.copyProperties(workOrder, workOrderdtldto);
        workOrderdtldto.setDetaillist(workOrderDtlService.findBySourceId(workOrder.getId()));

        return Result.ok(workOrderdtldto);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code) {

        LambdaQueryWrapper<WorkOrder> lambdaQueryWrapper = new LambdaQueryWrapper<WorkOrder>().eq(WorkOrder::getCode, code);
        WorkOrder workOrder = workOrderService.getOne(lambdaQueryWrapper);
        if (workOrder == null) {
            return Result.ok("未找到对应数据");
        }
        return Result.ok(workOrder);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WorkOrder workOrder) {
        return super.exportXls(request, workOrder, WorkOrder.class, "拆卸单列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WorkOrder.class);
    }

    @PutMapping("/updateStatus")
    public Result<?> updateStatus(@RequestBody WorkOrderDto workOrderdto) {

        WorkOrder workOrder = workOrderService.getById(workOrderdto.getId());
        workOrder.setBillStatus(workOrderdto.getBillStatus());
        workOrderService.updateById(workOrderdto);
        return Result.ok("操作成功！");
    }

}
