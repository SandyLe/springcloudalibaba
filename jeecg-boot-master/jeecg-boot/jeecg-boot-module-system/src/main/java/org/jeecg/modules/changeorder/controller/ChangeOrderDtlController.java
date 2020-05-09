package org.jeecg.modules.changeorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.changeorder.entity.ChangeOrderDtl;
import org.jeecg.modules.changeorder.service.ChangeOrderDtlService;
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
@RequestMapping("/changeOrderDtl")
public class ChangeOrderDtlController extends JeecgController<ChangeOrderDtl, ChangeOrderDtlService> {

    @Autowired
    private ChangeOrderDtlService changeOrderDtlService;

    @Autowired
    private MaterialService materialService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        changeOrderDtlService.removeById(id);
        return Result.ok("删除成功!");
    }
    @ApiOperation(value = "获取换货单数据", notes = "获取所有换货单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(ChangeOrderDtl changeOrderDtl, HttpServletRequest req) {
        QueryWrapper<ChangeOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(changeOrderDtl, req.getParameterMap());
        List<ChangeOrderDtl> changeOrderDtlList = changeOrderDtlService.list(queryWrapper);
        List<String> mtlIds = changeOrderDtlList.stream().map(ChangeOrderDtl::getMtlId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        changeOrderDtlList.stream().forEach(o->{
            o.setMaterial(mtlMap.get(o.getMtlId()));
        });
        return Result.ok(changeOrderDtlList);
    }
    @ApiOperation(value = "获取换货单产品", notes = "获取所有换货单产品")
    @GetMapping(value = "/getChangeOrderDtlOne")
    public Result<?> getChangeOrderDtlOne(ChangeOrderDtl changeOrderDtl, HttpServletRequest req) {
        QueryWrapper<ChangeOrderDtl> queryWrapper = QueryGenerator.initQueryWrapper(changeOrderDtl, req.getParameterMap());
        ChangeOrderDtl changeOrderDtl1 = changeOrderDtlService.getOne(queryWrapper);
        return Result.ok(changeOrderDtl1);
    }
}
