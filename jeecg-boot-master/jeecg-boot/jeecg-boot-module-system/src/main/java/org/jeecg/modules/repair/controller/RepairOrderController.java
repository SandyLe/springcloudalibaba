package org.jeecg.modules.repair.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.repair.dto.RepairOrderDto;
import org.jeecg.modules.repair.entity.RepairOrder;
import org.jeecg.modules.repair.service.RepairOrderDtlService;
import org.jeecg.modules.repair.service.RepairOrderService;
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
@RequestMapping("/repairOrder")
public class RepairOrderController extends JeecgController<RepairOrder, RepairOrderService> {
    @Autowired
    private RepairOrderService repairOrderService;
    @Autowired
    private RepairOrderDtlService repairOrderDtlService;
    @Autowired
    private ISysUserService iSysUserService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(RepairOrder repairOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<RepairOrder> queryWrapper = QueryGenerator.initQueryWrapper(repairOrder, req.getParameterMap());
        Page<RepairOrder> page = new Page<>(pageNo, pageSize);
        IPage<RepairOrder> pageList = repairOrderService.page(page, queryWrapper);
        List<RepairOrder> datas = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(datas)) {
            Map<String, String> userMap = new HashMap<>();

            List<String> userIds = datas.stream().map(RepairOrder::getOperateUserId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(userIds)) {
                Collection<SysUser> sysUsers = iSysUserService.listByIds(userIds);
                userMap.putAll(sysUsers.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getRealname)));
            }
            datas.stream().forEach(o -> {
                o.setOperateUserName(userMap.get(o.getOperateUserId()));
            });
        }
        pageList.setRecords(datas);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody RepairOrderDto repairOrderdto) {
        repairOrderService.saveOrder(repairOrderdto);
        return Result.ok();
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody RepairOrderDto repairOrderdto) {
        repairOrderService.editOrder(repairOrderdto);
        return Result.ok();
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        repairOrderService.removeById(id);
        repairOrderDtlService.deleteBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        repairOrderService.removeByIds(Arrays.asList(ids.split(",")));
        repairOrderDtlService.deleteBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/getOne")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        RepairOrder repairOrder = repairOrderService.getById(id);
        if (repairOrder == null) {
            return Result.ok("未找到对应数据");
        }
        RepairOrderDto repairOrderdtldto = new RepairOrderDto();
        BeanUtils.copyProperties(repairOrder, repairOrderdtldto);
        repairOrderdtldto.setDetaillist(repairOrderDtlService.findBySourceId(repairOrder.getId()));

        return Result.ok(repairOrderdtldto);
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
