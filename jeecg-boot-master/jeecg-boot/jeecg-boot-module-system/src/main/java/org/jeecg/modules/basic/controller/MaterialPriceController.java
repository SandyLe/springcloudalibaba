package org.jeecg.modules.basic.controller;

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
import org.jeecg.modules.basic.entity.MaterialPrice;
import org.jeecg.modules.basic.service.MaterialPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "产品价格")
@RestController
@RequestMapping("/materialPrice")
public class MaterialPriceController {

    @Autowired
    private MaterialPriceService materialPriceService;

    /**
     * 分页列表查询
     *
     * @param materialPrice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品价格数据列表", notes = "获取所有产品价格数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(MaterialPrice materialPrice, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<MaterialPrice> queryWrapper = QueryGenerator.initQueryWrapper(materialPrice, req.getParameterMap());
        Page<MaterialPrice> page = new Page<MaterialPrice>(pageNo, pageSize);
        IPage<MaterialPrice> pageList = materialPriceService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param materialPrice
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品价格数据", notes = "获取所有产品价格数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(MaterialPrice materialPrice, HttpServletRequest req) {
        QueryWrapper<MaterialPrice> queryWrapper = QueryGenerator.initQueryWrapper(materialPrice, req.getParameterMap());
        List<MaterialPrice> list = materialPriceService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param materialPrice
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加产品价格")
    @ApiOperation(value = "添加产品价格", notes = "添加产品价格")
    public Result<?> add(@RequestBody MaterialPrice materialPrice) {
        materialPriceService.save(materialPrice);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param materialPrice
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改产品价格")
    @ApiOperation(value = "修改产品价格", notes = "修改产品价格")
    public Result<?> edit(@RequestBody MaterialPrice materialPrice){
        materialPriceService.updateById(materialPrice);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除产品价格")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除产品价格", notes = "通过ID删除产品价格")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        materialPriceService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除产品价格", notes = "批量删除产品价格")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.materialPriceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询产品价格", notes = "通过ID查询产品价格")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        MaterialPrice materialPrice = materialPriceService.getById(id);
        return Result.ok(materialPrice);
    }
}
