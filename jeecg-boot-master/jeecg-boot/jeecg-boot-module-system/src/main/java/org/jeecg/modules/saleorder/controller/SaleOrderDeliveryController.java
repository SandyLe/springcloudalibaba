package org.jeecg.modules.saleorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.saleorder.entity.SaleOrderDeliveryInfo;
import org.jeecg.modules.saleorder.service.SaleOrderDeliveryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "单表销售订单发货信息")
@RestController
@RequestMapping("/saleOrderDeliveryInfo")
public class SaleOrderDeliveryController {

    @Autowired
    private SaleOrderDeliveryInfoService saleOrderDeliveryInfoService;
    /**
     * 添加
     *
     * @param saleOrderDeliveryInfo
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加销售订单发货信息")
    @ApiOperation(value = "添加销售订单发货信息", notes = "添加销售订单发货信息")
    public Result<?> add(@RequestBody SaleOrderDeliveryInfo saleOrderDeliveryInfo) {
        saleOrderDeliveryInfoService.save(saleOrderDeliveryInfo);
        return Result.ok(saleOrderDeliveryInfo.getId());
    }
    /**
     * 获取所有数据
     *
     * @param saleOrderDeliveryInfo
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单发货信息数据", notes = "获取所有销售订单发货信息数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrderDeliveryInfo saleOrderDeliveryInfo, HttpServletRequest req) {
        QueryWrapper<SaleOrderDeliveryInfo> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderDeliveryInfo, req.getParameterMap());
        List<SaleOrderDeliveryInfo> list = saleOrderDeliveryInfoService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 分页列表查询
     *
     * @param saleOrderDeliveryInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售订单发货信息数据列表", notes = "获取所有销售订单发货信息数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(SaleOrderDeliveryInfo saleOrderDeliveryInfo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SaleOrderDeliveryInfo> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderDeliveryInfo, req.getParameterMap());
        Page<SaleOrderDeliveryInfo> page = new Page<SaleOrderDeliveryInfo>(pageNo, pageSize);

        IPage<SaleOrderDeliveryInfo> pageList = saleOrderDeliveryInfoService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 修改
     *
     * @param saleOrderDeliveryInfo
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改销售订单发货信息")
    @ApiOperation(value = "修改销售订单发货信息", notes = "修改销售订单发货信息")
    public Result<?> edit(@RequestBody SaleOrderDeliveryInfo saleOrderDeliveryInfo){
        saleOrderDeliveryInfoService.updateById(saleOrderDeliveryInfo);
        return Result.ok(saleOrderDeliveryInfo);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除销售订单发货信息")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除销售订单发货信息", notes = "通过ID删除销售订单发货信息")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        saleOrderDeliveryInfoService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除销售订单发货信息", notes = "批量删除销售订单发货信息")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.saleOrderDeliveryInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询销售订单发货信息", notes = "通过ID查询销售订单发货信息")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        SaleOrderDeliveryInfo saleOrderDeliveryInfo = saleOrderDeliveryInfoService.getById(id);
        return Result.ok(saleOrderDeliveryInfo);
    }
}
