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
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.entity.Supplementary;
import org.jeecg.modules.basic.entity.SupplementaryValue;
import org.jeecg.modules.basic.service.SupplementaryService;
import org.jeecg.modules.basic.service.SupplementaryValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "辅助属性值")
@RestController
@RequestMapping("/supplementaryValue")
public class SupplementaryValueController {

    @Autowired
    private SupplementaryValueService supplementaryValueService;

    @Autowired
    private SupplementaryService supplementaryService;

    /**
     * 分页列表查询
     *
     * @param supplementaryValue
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取辅助属性值数据列表", notes = "获取所有辅助属性值数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(SupplementaryValue supplementaryValue, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SupplementaryValue> queryWrapper = QueryGenerator.initQueryWrapper(supplementaryValue, req.getParameterMap());
        Page<SupplementaryValue> page = new Page<SupplementaryValue>(pageNo, pageSize);
        IPage<SupplementaryValue> pageList = supplementaryValueService.page(page, queryWrapper);
        List<SupplementaryValue> data = pageList.getRecords();
        pageList.setRecords(data);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param supplementaryValue
     * @param req
     * @return
     */
    @ApiOperation(value = "获取辅助属性数据", notes = "获取所有辅助属性数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SupplementaryValue supplementaryValue, HttpServletRequest req) {
        QueryWrapper<SupplementaryValue> queryWrapper = QueryGenerator.initQueryWrapper(supplementaryValue, req.getParameterMap());
        List<SupplementaryValue> list = supplementaryValueService.list(queryWrapper);
        return Result.ok(list);
    }

    /**
     * 获取所有数据
     *
     * @param sourceCode
     * @return
     */
    @ApiOperation(value = "获取辅助属性数据", notes = "获取所有辅助属性数据")
    @GetMapping(value = "/getListBySourceCode")
    public Result<?> getListBySourceCode(String sourceCode) {

        Supplementary supplementary = supplementaryService.findByCcIdCode(LoginUtils.getLoginUser().getCompanyId(), sourceCode);
        LambdaQueryWrapper<SupplementaryValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SupplementaryValue::getSourceId, supplementary.getId());
        List<SupplementaryValue> list = supplementaryValueService.list(queryWrapper);
        return Result.ok(list);
    }

    /**
     * 添加
     *
     * @param supplementaryValue
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加辅助属性值")
    @ApiOperation(value = "添加辅助属性值", notes = "添加辅助属性值")
    public Result<?> add(@RequestBody SupplementaryValue supplementaryValue) {

        supplementaryValue.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        supplementaryValueService.save(supplementaryValue);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param supplementaryValue
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改辅助属性值")
    @ApiOperation(value = "修改辅助属性值", notes = "修改辅助属性值")
    public Result<?> edit(@RequestBody SupplementaryValue supplementaryValue){

        supplementaryValue.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
        supplementaryValueService.updateById(supplementaryValue);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除辅助属性值")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除辅助属性值", notes = "通过ID删除辅助属性值")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        supplementaryValueService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除辅助属性值", notes = "批量删除辅助属性值")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.supplementaryValueService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询辅助属性值", notes = "通过ID查询辅助属性值")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        SupplementaryValue supplementaryValue = supplementaryValueService.getById(id);
        return Result.ok(supplementaryValue);
    }
}
