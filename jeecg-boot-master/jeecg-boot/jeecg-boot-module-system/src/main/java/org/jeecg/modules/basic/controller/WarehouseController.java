package org.jeecg.modules.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Vendor;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "仓库接口")
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 分页列表查询
     *
     * @param warehouse
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取仓库数据列表", notes = "获取所有仓库据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(Warehouse warehouse, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Warehouse> queryWrapper = QueryGenerator.initQueryWrapper(warehouse, req.getParameterMap());
        Page<Warehouse> page = new Page<Warehouse>(pageNo, pageSize);

        IPage<Warehouse> pageList = warehouseService.page(page, queryWrapper);
        List<Warehouse> resultList = pageList.getRecords();
        List<String> shopIdList = resultList.stream().map(o->o.getBelongsToId()).collect(Collectors.toList());
        List<String> principalIdLIst = resultList.stream().map(o->o.getPrincipalId()).collect(Collectors.toList());
        Collection<SysDepart> shopList = sysDepartService.listByIds(shopIdList);
        Collection<SysUser> principalLIst = sysUserService.listByIds(principalIdLIst);
        Map<String, String> shopNameMap = shopList.stream().collect(Collectors.toMap(SysDepart::getId, SysDepart::getDepartName));
        Map<String, String> userNameMap = principalLIst.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getRealname));
        resultList.stream().forEach(o->{
            o.setBelongsToName(shopNameMap.get(o.getBelongsToId()));
            o.setPrincipalName(userNameMap.get(o.getPrincipalId()));
        });
        pageList.setRecords(resultList);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param warehouse
     * @param req
     * @return
     */
    @ApiOperation(value = "获取仓库数据列表", notes = "获取所有仓库据列表")
    @GetMapping(value = "/getList")
    public Result<?> getList(Warehouse warehouse, HttpServletRequest req) {
        QueryWrapper<Warehouse> queryWrapper = QueryGenerator.initQueryWrapper(warehouse, req.getParameterMap());
        List<Warehouse> list = warehouseService.list(queryWrapper);
        return Result.ok(list);
    }

    /**
     * 添加
     *
     * @param warehouse
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加仓库")
    @ApiOperation(value = "添加仓库", notes = "添加仓库")
    public Result<?> add(@RequestBody Warehouse warehouse) {
        warehouseService.save(warehouse);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param warehouse
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改仓库")
    @ApiOperation(value = "修改仓库", notes = "修改仓库")
    public Result<?> edit(@RequestBody Warehouse warehouse){
        warehouseService.updateById(warehouse);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除仓库")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除仓库", notes = "通过ID删除仓库")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        warehouseService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除仓库", notes = "批量删除仓库")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.warehouseService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询产品单位", notes = "通过ID查询产品单位")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        Warehouse warehouse = warehouseService.getById(id);
        return Result.ok(warehouse);
    }
}
