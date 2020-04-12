package org.jeecg.modules.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.inventory.entity.AllotDtl;
import org.jeecg.modules.inventory.service.AllotDtlService;
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
@Api(tags = "调拨产品")
@RestController
@RequestMapping("/allotDtl")
public class AllotDtlController extends JeecgController<AllotDtl, AllotDtlService> {

    @Autowired
    private AllotDtlService allotDtlService;

    @Autowired
    private MaterialService materialService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        allotDtlService.removeById(id);
        return Result.ok("删除成功!");
    }
    @ApiOperation(value = "获取调拨单数据", notes = "获取所有调拨单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(AllotDtl allotDtl, HttpServletRequest req) {
        QueryWrapper<AllotDtl> queryWrapper = QueryGenerator.initQueryWrapper(allotDtl, req.getParameterMap());
        List<AllotDtl> allotDtlList = allotDtlService.list(queryWrapper);
        List<String> mtlIds = allotDtlList.stream().map(AllotDtl::getMtlId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        allotDtlList.stream().forEach(o->{
            o.setMaterial(mtlMap.get(o.getMtlId()));
        });
        return Result.ok(allotDtlList);
    }
    @ApiOperation(value = "获取调拨单产品", notes = "获取所有调拨单产品")
    @GetMapping(value = "/getAllotDtlOne")
    public Result<?> getAllotDtlOne(AllotDtl allotDtl, HttpServletRequest req) {
        QueryWrapper<AllotDtl> queryWrapper = QueryGenerator.initQueryWrapper(allotDtl, req.getParameterMap());
        AllotDtl allotDtl1 = allotDtlService.getOne(queryWrapper);
        return Result.ok(allotDtl1);
    }
}
