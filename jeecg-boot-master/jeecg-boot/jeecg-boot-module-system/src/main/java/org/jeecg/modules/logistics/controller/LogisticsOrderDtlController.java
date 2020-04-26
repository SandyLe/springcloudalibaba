package org.jeecg.modules.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.logistics.entity.LogisticsOrderDtl;
import org.jeecg.modules.logistics.service.LogisticsOrderDtlService;
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
@Api(tags = "换货单产品")
@RestController
@RequestMapping("/logisticsOrderDtl")
public class LogisticsOrderDtlController extends JeecgController<LogisticsOrderDtl, LogisticsOrderDtlService> {

    @Autowired
    private LogisticsOrderDtlService logisticsOrderDtlService;

    @Autowired
    private MaterialService materialService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        logisticsOrderDtlService.removeById(id);
        return Result.ok("删除成功!");
    }
    @ApiOperation(value = "获取换货单数据", notes = "获取所有换货单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(LogisticsOrderDtl logisticsOrderDtl, HttpServletRequest req) {
        QueryWrapper<LogisticsOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(logisticsOrderDtl, req.getParameterMap());
        List<LogisticsOrderDtl> logisticsOrderDtlList = logisticsOrderDtlService.list(queryWrapper);
        List<String> mtlIds = logisticsOrderDtlList.stream().map(LogisticsOrderDtl::getMtlId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        logisticsOrderDtlList.stream().forEach(o->{
            o.setMaterial(mtlMap.get(o.getMtlId()));
        });
        return Result.ok(logisticsOrderDtlList);
    }
    @ApiOperation(value = "获取换货单产品", notes = "获取所有换货单产品")
    @GetMapping(value = "/getLogisticsOrderDtlOne")
    public Result<?> getLogisticsOrderDtlOne(LogisticsOrderDtl logisticsOrderDtl, HttpServletRequest req) {
        QueryWrapper<LogisticsOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(logisticsOrderDtl, req.getParameterMap());
        LogisticsOrderDtl logisticsOrderDtl1 = logisticsOrderDtlService.getOne(queryWrapper);
        return Result.ok(logisticsOrderDtl1);
    }
}
