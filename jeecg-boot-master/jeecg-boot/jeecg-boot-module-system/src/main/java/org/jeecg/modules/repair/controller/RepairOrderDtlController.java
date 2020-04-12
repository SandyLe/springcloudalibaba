package org.jeecg.modules.repair.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.repair.entity.RepairOrderDtl;
import org.jeecg.modules.repair.service.RepairOrderDtlService;
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
@Api(tags = "维修单产品")
@RestController
@RequestMapping("/repairOrderDtl")
public class RepairOrderDtlController extends JeecgController<RepairOrderDtl, RepairOrderDtlService> {

    @Autowired
    private RepairOrderDtlService repairOrderDtlService;

    @Autowired
    private MaterialService materialService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        repairOrderDtlService.removeById(id);
        return Result.ok("删除成功!");
    }
    @ApiOperation(value = "获取维修单数据", notes = "获取所有维修单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(RepairOrderDtl repairOrderDtl, HttpServletRequest req) {
        QueryWrapper<RepairOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(repairOrderDtl, req.getParameterMap());
        List<RepairOrderDtl> repairOrderDtlList = repairOrderDtlService.list(queryWrapper);
        List<String> mtlIds = repairOrderDtlList.stream().map(RepairOrderDtl::getMtlId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        repairOrderDtlList.stream().forEach(o->{
            o.setMaterial(mtlMap.get(o.getMtlId()));
        });
        return Result.ok(repairOrderDtlList);
    }
    @ApiOperation(value = "获取维修单产品", notes = "获取所有维修单产品")
    @GetMapping(value = "/getRepairOrderDtlOne")
    public Result<?> getRepairOrderDtlOne(RepairOrderDtl repairOrderDtl, HttpServletRequest req) {
        QueryWrapper<RepairOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(repairOrderDtl, req.getParameterMap());
        RepairOrderDtl repairOrderDtl1 = repairOrderDtlService.getOne(queryWrapper);
        return Result.ok(repairOrderDtl1);
    }
}
