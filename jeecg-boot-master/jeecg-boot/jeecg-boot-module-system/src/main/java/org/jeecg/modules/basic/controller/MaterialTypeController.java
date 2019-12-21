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
import org.jeecg.modules.basic.entity.MaterialType;
import org.jeecg.modules.basic.service.MaterialTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "产品类型")
@RestController
@RequestMapping("/materialType")
public class MaterialTypeController {

    @Autowired
    private MaterialTypeService materialTypeService;

    /**
     * 分页列表查询
     *
     * @param materialType
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品类型数据列表", notes = "获取所有产品类型数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(MaterialType materialType, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<MaterialType> queryWrapper = QueryGenerator.initQueryWrapper(materialType, req.getParameterMap());
        Page<MaterialType> page = new Page<MaterialType>(pageNo, pageSize);
        IPage<MaterialType> pageList = materialTypeService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param materialType
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品类型数据", notes = "获取所有产品类型数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(MaterialType materialType, HttpServletRequest req) {
        QueryWrapper<MaterialType> queryWrapper = QueryGenerator.initQueryWrapper(materialType, req.getParameterMap());
        List<MaterialType> list = materialTypeService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param materialType
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加产品类型")
    @ApiOperation(value = "添加产品类型", notes = "添加产品类型")
    public Result<?> add(@RequestBody MaterialType materialType) {
        materialTypeService.save(materialType);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param materialType
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改产品类型")
    @ApiOperation(value = "修改产品类型", notes = "修改产品类型")
    public Result<?> edit(@RequestBody MaterialType materialType){
        materialTypeService.updateById(materialType);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除产品类型")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除产品类型", notes = "通过ID删除产品类型")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        materialTypeService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除产品类型", notes = "批量删除产品类型")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.materialTypeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询产品类型", notes = "通过ID查询产品类型")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        MaterialType materialType = materialTypeService.getById(id);
        return Result.ok(materialType);
    }
}
