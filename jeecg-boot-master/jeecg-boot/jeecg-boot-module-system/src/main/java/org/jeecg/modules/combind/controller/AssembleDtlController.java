package org.jeecg.modules.combind.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.combind.entity.AssembleDtl;
import org.jeecg.modules.combind.service.AssembleDtlService;
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
@Api(tags = "组装单产品")
@RestController
@RequestMapping("/assembleDtl")
public class AssembleDtlController extends JeecgController<AssembleDtl, AssembleDtlService> {

    @Autowired
    private AssembleDtlService assembleDtlService;

    @Autowired
    private MaterialService materialService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        assembleDtlService.removeById(id);
        return Result.ok("删除成功!");
    }
    @ApiOperation(value = "获取组装单数据", notes = "获取所有组装单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(AssembleDtl assembleDtl, HttpServletRequest req) {
        QueryWrapper<AssembleDtl> queryWrapper = QueryGenerator.initQueryWrapper(assembleDtl, req.getParameterMap());
        List<AssembleDtl> assembleDtlList = assembleDtlService.list(queryWrapper);
        List<String> mtlIds = assembleDtlList.stream().map(AssembleDtl::getMtlId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        assembleDtlList.stream().forEach(o->{
            o.setMaterial(mtlMap.get(o.getMtlId()));
        });
        return Result.ok(assembleDtlList);
    }
    @ApiOperation(value = "获取组装单产品", notes = "获取所有组装单产品")
    @GetMapping(value = "/getAssembleDtlOne")
    public Result<?> getAssembleDtlOne(AssembleDtl assembleDtl, HttpServletRequest req) {
        QueryWrapper<AssembleDtl> queryWrapper = QueryGenerator.initQueryWrapper(assembleDtl, req.getParameterMap());
        AssembleDtl assembleDtl1 = assembleDtlService.getOne(queryWrapper);
        return Result.ok(assembleDtl1);
    }
}
