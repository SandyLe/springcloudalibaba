package org.jeecg.modules.purchase.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.purchase.entity.PurchaseMtl;
import org.jeecg.modules.purchase.service.PurchaseMtlService;
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
 * @Author: tomkluas
 * @Date: 2020/1/14 14:32
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "采购列表")
@RestController
@RequestMapping("/purchaseMtl")
public class PurchaseMtlController extends JeecgController<PurchaseMtl, PurchaseMtlService> {

    @Autowired
    private PurchaseMtlService purchaseMtlService;

    @Autowired
    private MaterialService materialService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        purchaseMtlService.removeById(id);
        return Result.ok("删除成功!");
    }
    @ApiOperation(value = "获取采购订单数据", notes = "获取所有采购订单数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(PurchaseMtl purchaseMtl, HttpServletRequest req) {
        QueryWrapper<PurchaseMtl> queryWrapper = QueryGenerator.initQueryWrapper(purchaseMtl, req.getParameterMap());
        List<PurchaseMtl> purchaseMtlList = purchaseMtlService.list(queryWrapper);
        List<String> mtlIds = purchaseMtlList.stream().map(PurchaseMtl::getMtlId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        purchaseMtlList.stream().forEach(o->{
            o.setMtl(mtlMap.get(o.getMtlId()));
        });
        return Result.ok(purchaseMtlList);
    }
    @ApiOperation(value = "获取采购订单产品", notes = "获取所有采购订单产品")
    @GetMapping(value = "/getPurchaseMtlOne")
    public Result<?> getPurchaseMtlOne(PurchaseMtl purchaseMtl, HttpServletRequest req) {
        QueryWrapper<PurchaseMtl> queryWrapper = QueryGenerator.initQueryWrapper(purchaseMtl, req.getParameterMap());
        PurchaseMtl purchaseMtl1 = purchaseMtlService.getOne(queryWrapper);
        return Result.ok(purchaseMtl1);
    }
}
