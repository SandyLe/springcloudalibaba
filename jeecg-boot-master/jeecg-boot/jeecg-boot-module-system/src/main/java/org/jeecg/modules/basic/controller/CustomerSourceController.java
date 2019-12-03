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
import org.jeecg.modules.basic.entity.CustomerSource;
import org.jeecg.modules.basic.service.CustomerSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "客户来源")
@RestController
@RequestMapping("/customerSource")
public class CustomerSourceController {

    @Autowired
    private CustomerSourceService customerSourceService;

    /**
     * 分页列表查询
     *
     * @param customerType
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取客户来源数据列表", notes = "获取所有客户来源数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(CustomerSource customerType, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<CustomerSource> queryWrapper = QueryGenerator.initQueryWrapper(customerType, req.getParameterMap());
        Page<CustomerSource> page = new Page<CustomerSource>(pageNo, pageSize);

        IPage<CustomerSource> pageList = customerSourceService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param customerType
     * @param req
     * @return
     */
    @ApiOperation(value = "获取客户来源数据", notes = "获取所有客户来源数据")
    @GetMapping(value = "/getList")
    public Result<List<CustomerSource>> getList(CustomerSource customerType, HttpServletRequest req) {
        QueryWrapper<CustomerSource> queryWrapper = QueryGenerator.initQueryWrapper(customerType, req.getParameterMap());
        List<CustomerSource> list = customerSourceService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param customerSource
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加客户来源")
    @ApiOperation(value = "添加客户来源", notes = "添加客户来源")
    public Result<?> add(@RequestBody CustomerSource customerSource) {
        customerSourceService.save(customerSource);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param customerSource
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改客户来源")
    @ApiOperation(value = "修改客户来源", notes = "修改客户来源")
    public Result<?> edit(@RequestBody CustomerSource customerSource){
        customerSourceService.updateById(customerSource);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除客户来源")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除客户来源", notes = "通过ID删除客户来源")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        customerSourceService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除客户来源", notes = "批量删除客户来源")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.customerSourceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询客户来源", notes = "通过ID查询客户来源")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        CustomerSource customerSource = customerSourceService.getById(id);
        return Result.ok(customerSource);
    }
}
