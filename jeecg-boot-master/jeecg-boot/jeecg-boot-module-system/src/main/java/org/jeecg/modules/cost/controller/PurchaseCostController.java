package org.jeecg.modules.cost.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.jeecg.modules.cost.entity.PurchaseCost;
import org.jeecg.modules.cost.service.PurchaseCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "采购批次均价")
@RestController
@RequestMapping("/purchaseCost")
public class PurchaseCostController {

    @Autowired
    private PurchaseCostService purchaseCostService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialUnitService materialUnitService;
    /**
     * 添加
     *
     * @param purchaseCost
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加采购批次平均价格")
    @ApiOperation(value = "添加采购批次平均价格", notes = "添加采购批次平均价格")
    public Result<?> add(@RequestBody PurchaseCost purchaseCost) {
        PurchaseCost existCode = purchaseCostService.getOne(new LambdaQueryWrapper<PurchaseCost>().eq(PurchaseCost::getCode, purchaseCost.getCode()).ne(PurchaseCost::getId, purchaseCost.getId()));
        Assert.isNull(existCode, "单号已存在！");
        purchaseCostService.save(purchaseCost);
        Result<Object> result = Result.ok();
        result.setResult(purchaseCost);
        return result;
    }
    /**
     * 获取所有数据
     *
     * @param purchaseCost
     * @param req
     * @return
     */
    @ApiOperation(value = "获取采购批次平均价格数据", notes = "获取所有采购批次平均价格数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(PurchaseCost purchaseCost, HttpServletRequest req) {
        QueryWrapper<PurchaseCost> queryWrapper = QueryGenerator.initQueryWrapper(purchaseCost, req.getParameterMap());
        List<PurchaseCost> list = purchaseCostService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param purchaseCost
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取采购批次平均价格数据列表", notes = "获取所有采购批次平均价格数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(PurchaseCost purchaseCost, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<PurchaseCost> queryWrapper = QueryGenerator.initQueryWrapper(purchaseCost, req.getParameterMap());
        Page<PurchaseCost> page = new Page<PurchaseCost>(pageNo, pageSize);

        IPage<PurchaseCost> pageList = purchaseCostService.page(page, queryWrapper);
        List<PurchaseCost> purchaseCostList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(purchaseCostList)) {
            List<String> mtlIds = purchaseCostList.stream().map(PurchaseCost::getMtlId).collect(Collectors.toList());
            List<String> unitIds = purchaseCostList.stream().map(PurchaseCost::getUnitId).collect(Collectors.toList());
            Collection<Material> materials = materialService.listByIds(mtlIds);
            Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
            Map<String, String> materialMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
            Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
            purchaseCostList.stream().forEach(o->{
                o.setMaterial(materialMap.get(o.getMtlId()));
                o.setUnit(unitMap.get(o.getUnitId()));
            });
        }

        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 修改
     *
     * @param purchaseCost
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改采购批次平均价格")
    @ApiOperation(value = "修改采购批次平均价格", notes = "修改采购批次平均价格")
    public Result<?> edit(@RequestBody PurchaseCost purchaseCost){
        PurchaseCost existCode = purchaseCostService.getOne(new LambdaQueryWrapper<PurchaseCost>().eq(PurchaseCost::getCode, purchaseCost.getCode()).ne(PurchaseCost::getId, purchaseCost.getId()));
        Assert.isNull(existCode, "单号已存在！");
        purchaseCostService.updateById(purchaseCost);
        Result<Object> result = Result.ok();
        result.setResult(purchaseCost);
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除采购批次平均价格")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除采购批次平均价格", notes = "通过ID删除采购批次平均价格")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        purchaseCostService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除采购批次平均价格", notes = "批量删除采购批次平均价格")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.purchaseCostService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询采购批次平均价格", notes = "通过ID查询采购批次平均价格")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        PurchaseCost purchaseCost = purchaseCostService.getById(id);
        return Result.ok(purchaseCost);
    }


}
