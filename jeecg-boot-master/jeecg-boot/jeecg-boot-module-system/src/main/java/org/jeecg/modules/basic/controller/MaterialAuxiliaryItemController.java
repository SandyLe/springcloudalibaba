package org.jeecg.modules.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.entity.MaterialAuxiliary;
import org.jeecg.modules.basic.entity.MaterialAuxiliaryItem;
import org.jeecg.modules.basic.entity.Supplementary;
import org.jeecg.modules.basic.entity.SupplementaryValue;
import org.jeecg.modules.basic.service.MaterialAuxiliaryItemService;
import org.jeecg.modules.basic.service.MaterialAuxiliaryService;
import org.jeecg.modules.basic.service.SupplementaryService;
import org.jeecg.modules.basic.service.SupplementaryValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "产品辅助属性条目条目")
@RestController
@RequestMapping("/materialAuxiliaryItem")
public class MaterialAuxiliaryItemController {

    @Autowired
    private MaterialAuxiliaryItemService materialAuxiliaryItemService;

    @Autowired
    private MaterialAuxiliaryService materialAuxiliaryService;

    @Autowired
    private SupplementaryService supplementaryService;

    @Autowired
    private SupplementaryValueService supplementaryValueService;

    /**
     * 分页列表查询
     *
     * @param materialAuxiliaryItem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取辅助属性条目数据列表", notes = "获取所有辅助属性条目数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(MaterialAuxiliaryItem materialAuxiliaryItem, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<MaterialAuxiliaryItem> queryWrapper = QueryGenerator.initQueryWrapper(materialAuxiliaryItem, req.getParameterMap());
        Page<MaterialAuxiliaryItem> page = new Page<MaterialAuxiliaryItem>(pageNo, pageSize);
        IPage<MaterialAuxiliaryItem> pageList = materialAuxiliaryItemService.page(page, queryWrapper);
        List<MaterialAuxiliaryItem> data = pageList.getRecords();
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
     * @param materialAuxiliaryItem
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品辅助属性条目条目数据", notes = "获取所有产品辅助属性条目条目数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(MaterialAuxiliaryItem materialAuxiliaryItem, HttpServletRequest req) {
        QueryWrapper<MaterialAuxiliaryItem> queryWrapper = QueryGenerator.initQueryWrapper(materialAuxiliaryItem, req.getParameterMap());
        List<MaterialAuxiliaryItem> list = materialAuxiliaryItemService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param materialAuxiliaryItem
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加辅助属性条目")
    @ApiOperation(value = "添加辅助属性条目", notes = "添加辅助属性条目")
    public Result<?> add(@RequestBody MaterialAuxiliaryItem materialAuxiliaryItem) {

        Supplementary supplementary = supplementaryService.findByCcIdCode(LoginUtils.getLoginUser().getCompanyId(), materialAuxiliaryItem.getSuppCode());
        SupplementaryValue supplementaryValue = supplementaryValueService.findBySourceIdCode(supplementary.getId(), materialAuxiliaryItem.getSuppValueCode());
        materialAuxiliaryItem.setSuppName(supplementary.getName());
        materialAuxiliaryItem.setSuppValueName(supplementaryValue.getName());
        MaterialAuxiliary materialAuxiliary = null;
        if (StringUtils.isBlank(materialAuxiliaryItem.getSourceId())) {
            materialAuxiliary = new MaterialAuxiliary();
            materialAuxiliary.setSuppCodeMap(materialAuxiliaryItem.getSuppCode() + ":" + materialAuxiliaryItem.getSuppValueCode() + ";");
            materialAuxiliary.setSuppValueMap(materialAuxiliaryItem.getSuppName() + ":" +materialAuxiliaryItem.getSuppValueName() + ";");
            materialAuxiliary.setSourceId(materialAuxiliaryItem.getMtlId());
            materialAuxiliary.setCompanyId(LoginUtils.getLoginUser().getCompanyId());
            materialAuxiliaryService.save(materialAuxiliary);
            materialAuxiliaryItem.setSourceId(materialAuxiliary.getId());
            materialAuxiliaryItemService.save(materialAuxiliaryItem);
        } else {
            materialAuxiliary = materialAuxiliaryService.getById(materialAuxiliaryItem.getSourceId());
            materialAuxiliaryItem.setSourceId(materialAuxiliary.getId());
            materialAuxiliaryItemService.save(materialAuxiliaryItem);

            LambdaQueryWrapper<MaterialAuxiliaryItem> queryWrapper = new LambdaQueryWrapper<MaterialAuxiliaryItem>();
            queryWrapper.eq(MaterialAuxiliaryItem::getSourceId, materialAuxiliary.getId());
            queryWrapper.orderByAsc(MaterialAuxiliaryItem::getSuppCode);
            List<MaterialAuxiliaryItem> items = materialAuxiliaryItemService.list(queryWrapper);
            StringBuffer suppNameBuffer = new StringBuffer();
            StringBuffer suppCodeBuffer = new StringBuffer();
            items.stream().forEach(o->{
                suppCodeBuffer.append(o.getSuppCode() + ":" + o.getSuppValueCode() + ";");
                suppNameBuffer.append(o.getSuppName() + ":" + o.getSuppValueName() + ";");
            });
            materialAuxiliary.setSuppCodeMap(suppCodeBuffer.toString());
            materialAuxiliary.setSuppValueMap(suppNameBuffer.toString());
            materialAuxiliaryService.saveOrUpdate(materialAuxiliary);
        }
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param materialAuxiliaryItem
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改辅助属性条目")
    @ApiOperation(value = "修改辅助属性条目", notes = "修改辅助属性条目")
    public Result<?> edit(@RequestBody MaterialAuxiliaryItem materialAuxiliaryItem){

        Supplementary supplementary = supplementaryService.findByCcIdCode(LoginUtils.getLoginUser().getCompanyId(), materialAuxiliaryItem.getSuppCode());
        SupplementaryValue supplementaryValue = supplementaryValueService.findBySourceIdCode(supplementary.getId(), materialAuxiliaryItem.getSuppValueCode());
        materialAuxiliaryItem.setSuppName(supplementary.getName());
        materialAuxiliaryItem.setSuppValueName(supplementaryValue.getName());
        MaterialAuxiliary materialAuxiliary = materialAuxiliaryService.getById(materialAuxiliaryItem.getSourceId());
        materialAuxiliaryItem.setSourceId(materialAuxiliary.getId());
        materialAuxiliaryItemService.updateById(materialAuxiliaryItem);

        LambdaQueryWrapper<MaterialAuxiliaryItem> queryWrapper = new LambdaQueryWrapper<MaterialAuxiliaryItem>();
        queryWrapper.eq(MaterialAuxiliaryItem::getSourceId, materialAuxiliary.getId());
        queryWrapper.orderByAsc(MaterialAuxiliaryItem::getSuppCode);
        List<MaterialAuxiliaryItem> items = materialAuxiliaryItemService.list(queryWrapper);
        StringBuffer suppNameBuffer = new StringBuffer();
        StringBuffer suppCodeBuffer = new StringBuffer();
        items.stream().forEach(o->{
            suppCodeBuffer.append(o.getSuppCode() + ":" + o.getSuppValueCode() + ";");
            suppNameBuffer.append(o.getSuppName() + ":" + o.getSuppValueName() + ";");
        });
        materialAuxiliary.setSuppCodeMap(suppCodeBuffer.toString());
        materialAuxiliary.setSuppValueMap(suppNameBuffer.toString());
        materialAuxiliaryService.saveOrUpdate(materialAuxiliary);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除辅助属性条目")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除辅助属性条目", notes = "通过ID删除辅助属性条目")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {

        MaterialAuxiliaryItem materialAuxiliaryItem = materialAuxiliaryItemService.getById(id);
        MaterialAuxiliary materialAuxiliary = materialAuxiliaryService.getById(materialAuxiliaryItem.getSourceId());

        materialAuxiliaryItemService.removeById(id);

        LambdaQueryWrapper<MaterialAuxiliaryItem> queryWrapper = new LambdaQueryWrapper<MaterialAuxiliaryItem>();
        queryWrapper.eq(MaterialAuxiliaryItem::getSourceId, materialAuxiliary.getId());
        queryWrapper.orderByAsc(MaterialAuxiliaryItem::getSuppCode);
        List<MaterialAuxiliaryItem> items = materialAuxiliaryItemService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(items)) {
            StringBuffer suppNameBuffer = new StringBuffer();
            StringBuffer suppCodeBuffer = new StringBuffer();
            items.stream().forEach(o->{
                suppCodeBuffer.append(o.getSuppCode() + ":" + o.getSuppValueCode() + ";");
                suppNameBuffer.append(o.getSuppName() + ":" + o.getSuppValueName() + ";");
            });
            materialAuxiliary.setSuppCodeMap(suppCodeBuffer.toString());
            materialAuxiliary.setSuppValueMap(suppNameBuffer.toString());
            materialAuxiliaryService.saveOrUpdate(materialAuxiliary);
        } else {
            materialAuxiliaryService.removeById(materialAuxiliaryItem.getSourceId());
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除辅助属性条目", notes = "批量删除辅助属性条目")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        List<String> idList = Arrays.asList(ids.split(","));

        MaterialAuxiliaryItem materialAuxiliaryItem = materialAuxiliaryItemService.getById(idList.get(0));
        MaterialAuxiliary materialAuxiliary = materialAuxiliaryService.getById(materialAuxiliaryItem.getSourceId());

        this.materialAuxiliaryItemService.removeByIds(idList);

        LambdaQueryWrapper<MaterialAuxiliaryItem> queryWrapper = new LambdaQueryWrapper<MaterialAuxiliaryItem>();
        queryWrapper.eq(MaterialAuxiliaryItem::getSourceId, materialAuxiliary.getId());
        queryWrapper.orderByAsc(MaterialAuxiliaryItem::getSuppCode);
        List<MaterialAuxiliaryItem> items = materialAuxiliaryItemService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(items)) {
            StringBuffer suppNameBuffer = new StringBuffer();
            StringBuffer suppCodeBuffer = new StringBuffer();
            items.stream().forEach(o->{
                suppCodeBuffer.append(o.getSuppCode() + ":" + o.getSuppValueCode() + ";");
                suppNameBuffer.append(o.getSuppName() + ":" + o.getSuppValueName() + ";");
            });
            materialAuxiliary.setSuppCodeMap(suppCodeBuffer.toString());
            materialAuxiliary.setSuppValueMap(suppNameBuffer.toString());
            materialAuxiliaryService.saveOrUpdate(materialAuxiliary);
        } else {
            materialAuxiliaryService.removeById(materialAuxiliaryItem.getSourceId());
        }

        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询辅助属性条目", notes = "通过ID查询辅助属性条目")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        MaterialAuxiliaryItem materialAuxiliaryItem = materialAuxiliaryItemService.getById(id);
        return Result.ok(materialAuxiliaryItem);
    }

}
