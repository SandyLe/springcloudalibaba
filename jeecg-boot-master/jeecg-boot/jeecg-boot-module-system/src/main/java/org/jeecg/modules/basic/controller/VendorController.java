package org.jeecg.modules.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.basic.entity.Vendor;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "供应商")
@RestController
@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    private VendorService vendorService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;


    /**
     * 分页列表查询
     *
     * @param vendor
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取供应商数据列表", notes = "获取所有供应商数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(Vendor vendor, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Vendor> queryWrapper = QueryGenerator.initQueryWrapper(vendor, req.getParameterMap());
        Page<Vendor> page = new Page<Vendor>(pageNo, pageSize);

        IPage<Vendor> pageList = vendorService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param vendor
     * @param req
     * @return
     */
    @ApiOperation(value = "获取客户来源数据", notes = "获取所有客户来源数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(Vendor vendor, HttpServletRequest req) {
        QueryWrapper<Vendor> queryWrapper = QueryGenerator.initQueryWrapper(vendor, req.getParameterMap());
        List<Vendor> list = vendorService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param vendor
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加供应商")
    @ApiOperation(value = "添加供应商", notes = "添加供应商")
    public Result<?> add(@RequestBody Vendor vendor) {
        if (StringUtils.isEmpty(vendor.getId())) {
            vendor.setCode(billCodeBuilderService.getBillCode(BillType.VENDOR.getId()));
        }
        Vendor existCode = vendorService.getOne(new LambdaQueryWrapper<Vendor>().eq(Vendor::getCode, vendor.getCode()).ne(Vendor::getId, vendor.getId()));
        Assert.isNull(existCode, "编号已存在！");
        vendorService.save(vendor);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param vendor
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改供应商")
    @ApiOperation(value = "修改供应商", notes = "修改供应商")
    public Result<?> edit(@RequestBody Vendor vendor){
        Vendor existCode = vendorService.getOne(new LambdaQueryWrapper<Vendor>().eq(Vendor::getCode, vendor.getCode()).ne(Vendor::getId, vendor.getId()));
        Assert.isNull(existCode, "编号已存在！");
        vendorService.updateById(vendor);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除供应商")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除供应商", notes = "通过ID删除供应商")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        vendorService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除供应商", notes = "批量删除供应商")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.vendorService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询供应商", notes = "通过ID查询供应商")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        Vendor vendor = vendorService.getById(id);
        return Result.ok(vendor);
    }
}
