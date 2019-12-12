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
import org.jeecg.modules.basic.entity.MaterialBrand;
import org.jeecg.modules.basic.service.MaterialBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "产品品牌")
@RestController
@RequestMapping("/materialBrand")
public class MaterialBrandController {

    @Autowired
    private MaterialBrandService materialBrandService;

    /**
     * 分页列表查询
     *
     * @param materialBrand
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品品牌数据列表", notes = "获取所有产品品牌数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(MaterialBrand materialBrand, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<MaterialBrand> queryWrapper = QueryGenerator.initQueryWrapper(materialBrand, req.getParameterMap());
        Page<MaterialBrand> page = new Page<MaterialBrand>(pageNo, pageSize);

        IPage<MaterialBrand> pageList = materialBrandService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param materialBrand
     * @param req
     * @return
     */
    @ApiOperation(value = "获取品牌数据", notes = "获取所有品牌数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(MaterialBrand materialBrand, HttpServletRequest req) {
        QueryWrapper<MaterialBrand> queryWrapper = QueryGenerator.initQueryWrapper(materialBrand, req.getParameterMap());
        List<MaterialBrand> list = materialBrandService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param materialBrand
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加产品品牌")
    @ApiOperation(value = "添加产品品牌", notes = "添加产品品牌")
    public Result<?> add(@RequestBody MaterialBrand materialBrand) {
        materialBrandService.save(materialBrand);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param materialBrand
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改产品品牌")
    @ApiOperation(value = "修改产品品牌", notes = "修改产品品牌")
    public Result<?> edit(@RequestBody MaterialBrand materialBrand){
        materialBrandService.updateById(materialBrand);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除产品品牌")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除产品品牌", notes = "通过ID删除产品品牌")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        materialBrandService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除产品品牌", notes = "批量删除产品品牌")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.materialBrandService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询产品品牌", notes = "通过ID查询产品品牌")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        MaterialBrand materialBrand = materialBrandService.getById(id);
        return Result.ok(materialBrand);
    }
}
