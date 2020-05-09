package org.jeecg.modules.workorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.workorder.entity.WorkOrderDtl;
import org.jeecg.modules.workorder.service.WorkOrderDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/12 14:32
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "工单产品")
@RestController
@RequestMapping("/workOrderDtl")
public class WorkOrderDtlController extends JeecgController<WorkOrderDtl, WorkOrderDtlService> {

    @Autowired
    private WorkOrderDtlService workOrderDtlService;

    @Autowired
    private MaterialService materialService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        workOrderDtlService.removeById(id);
        return Result.ok("删除成功!");
    }
    @ApiOperation(value = "获取工单数据", notes = "获取所有工单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(WorkOrderDtl workOrderDtl, HttpServletRequest req) {
        QueryWrapper<WorkOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(workOrderDtl, req.getParameterMap());
        List<WorkOrderDtl> workOrderDtlList = workOrderDtlService.list(queryWrapper);
        List<String> mtlIds = workOrderDtlList.stream().map(WorkOrderDtl::getMtlId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        workOrderDtlList.stream().forEach(o->{
            o.setMaterial(mtlMap.get(o.getMtlId()));
        });
        return Result.ok(workOrderDtlList);
    }
    @ApiOperation(value = "获取工单产品", notes = "获取所有工单产品")
    @GetMapping(value = "/getWorkOrderDtlOne")
    public Result<?> getWorkOrderDtlOne(WorkOrderDtl workOrderDtl, HttpServletRequest req) {
        QueryWrapper<WorkOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(workOrderDtl, req.getParameterMap());
        WorkOrderDtl workOrderDtl1 = workOrderDtlService.getOne(queryWrapper);
        return Result.ok(workOrderDtl1);
    }
}
