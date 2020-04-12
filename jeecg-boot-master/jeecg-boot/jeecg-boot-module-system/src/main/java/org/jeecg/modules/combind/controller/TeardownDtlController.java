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
import org.jeecg.modules.combind.entity.TeardownDtl;
import org.jeecg.modules.combind.service.TeardownDtlService;
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
@Api(tags = "拆卸单产品")
@RestController
@RequestMapping("/teardownDtl")
public class TeardownDtlController extends JeecgController<TeardownDtl, TeardownDtlService> {

    @Autowired
    private TeardownDtlService teardownDtlService;

    @Autowired
    private MaterialService materialService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        teardownDtlService.removeById(id);
        return Result.ok("删除成功!");
    }
    @ApiOperation(value = "获取拆卸单数据", notes = "获取所有拆卸单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(TeardownDtl teardownDtl, HttpServletRequest req) {
        QueryWrapper<TeardownDtl> queryWrapper = QueryGenerator.initQueryWrapper(teardownDtl, req.getParameterMap());
        List<TeardownDtl> teardownDtlList = teardownDtlService.list(queryWrapper);
        List<String> mtlIds = teardownDtlList.stream().map(TeardownDtl::getMtlId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        teardownDtlList.stream().forEach(o->{
            o.setMaterial(mtlMap.get(o.getMtlId()));
        });
        return Result.ok(teardownDtlList);
    }
    @ApiOperation(value = "获取拆卸单产品", notes = "获取所有拆卸单产品")
    @GetMapping(value = "/getTeardownDtlOne")
    public Result<?> getTeardownDtlOne(TeardownDtl teardownDtl, HttpServletRequest req) {
        QueryWrapper<TeardownDtl> queryWrapper = QueryGenerator.initQueryWrapper(teardownDtl, req.getParameterMap());
        TeardownDtl teardownDtl1 = teardownDtlService.getOne(queryWrapper);
        return Result.ok(teardownDtl1);
    }
}
