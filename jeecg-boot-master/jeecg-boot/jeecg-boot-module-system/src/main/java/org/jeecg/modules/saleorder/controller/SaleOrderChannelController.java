package org.jeecg.modules.saleorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.saleorder.entity.SaleOrderChannel;
import org.jeecg.modules.saleorder.service.SaleOrderChannelService;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.model.SysPermissionTree;
import org.jeecg.modules.system.model.TreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Api(tags = "销售渠道")
@RestController
@RequestMapping("/saleOrderChannel")
public class SaleOrderChannelController {

    @Autowired
    private SaleOrderChannelService saleOrderChannelService;

    /**
     * 分页列表查询
     *
     * @param saleOrderChannel
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售渠道数据列表", notes = "获取所有销售渠道数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(SaleOrderChannel saleOrderChannel, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SaleOrderChannel> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderChannel, req.getParameterMap());
        Page<SaleOrderChannel> page = new Page<SaleOrderChannel>(pageNo, pageSize);

        IPage<SaleOrderChannel> pageList = saleOrderChannelService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param saleOrderChannel
     * @param req
     * @return
     */
    @ApiOperation(value = "获取销售渠道数据", notes = "获取所有销售渠道数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(SaleOrderChannel saleOrderChannel, HttpServletRequest req) {
        QueryWrapper<SaleOrderChannel> queryWrapper = QueryGenerator.initQueryWrapper(saleOrderChannel, req.getParameterMap());
        List<SaleOrderChannel> list = saleOrderChannelService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param saleOrderChannel
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加销售渠道")
    @ApiOperation(value = "添加销售渠道", notes = "添加销售渠道")
    public Result<?> add(@RequestBody SaleOrderChannel saleOrderChannel) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(saleOrderChannel.getCompanyId())) {
            saleOrderChannel.setCompanyId(sysUser.getCompanyId());
        }
        saleOrderChannelService.save(saleOrderChannel);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param saleOrderChannel
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改销售渠道")
    @ApiOperation(value = "修改销售渠道", notes = "修改销售渠道")
    public Result<?> edit(@RequestBody SaleOrderChannel saleOrderChannel){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(saleOrderChannel.getCompanyId())) {
            saleOrderChannel.setCompanyId(sysUser.getCompanyId());
        }
        saleOrderChannelService.updateById(saleOrderChannel);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除销售渠道")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除销售渠道", notes = "通过ID删除销售渠道")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        saleOrderChannelService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除销售渠道", notes = "批量删除销售渠道")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.saleOrderChannelService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询销售渠道", notes = "通过ID查询销售渠道")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        SaleOrderChannel saleOrderChannel = saleOrderChannelService.getById(id);
        return Result.ok(saleOrderChannel);
    }



    /* 先查询一级渠道，当用户点击展开渠道时加载子渠道 */
    /**
     * 系统渠道列表(一级渠道)
     *
     * @return
     */
    @RequestMapping(value = "/getRootChannel", method = RequestMethod.GET)
    public Result<List<SysPermissionTree>> getRootChannelList(SaleOrderChannel saleOrderChannel, HttpServletRequest req) {
        long start = System.currentTimeMillis();
        Result<List<SysPermissionTree>> result = new Result<>();
        try {
            QueryWrapper<SaleOrderChannel> query = QueryGenerator.initQueryWrapper(saleOrderChannel, req.getParameterMap());
            query.orderByAsc("create_time");
            List<SaleOrderChannel> list = saleOrderChannelService.list(query);

            List<SysPermissionTree> sysPermissionList = list.stream().filter(p->StringUtils.isEmpty(p.getParentId())).map(p->{
                SysPermissionTree model = new SysPermissionTree();
                model.setId(p.getId());
                model.setKey(p.getId());
                model.setName(p.getName());
                model.setLeaf(!list.stream().anyMatch(m-> StringUtils.equals(m.getParentId(), p.getId())));
                if(!model.getIsLeaf()){
                    model.setChildren(new ArrayList<>());
                }

                return model;
            }).collect(Collectors.toList());
            result.setResult(sysPermissionList);

            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("======获取一级渠道数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        return result;
    }


    /**
     * 查询子渠道
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getSubChannel", method = RequestMethod.GET)
    public Result<List<SysPermissionTree>> getSubChannel(@RequestParam("parentId") String parentId){
        Result<List<SysPermissionTree>> result = new Result<>();
        try{
            LambdaQueryWrapper<SaleOrderChannel> query = new LambdaQueryWrapper<SaleOrderChannel>();
            query.orderByDesc(SaleOrderChannel::getCreateTime);
            List<SaleOrderChannel> list = saleOrderChannelService.list(query);

            List<SysPermissionTree> sysPermissionList = list.stream().filter(p->StringUtils.equals(p.getParentId(), parentId)).map(p->{
                SysPermissionTree model = new SysPermissionTree();
                model.setId(p.getId());
                model.setKey(p.getId());
                model.setName(p.getName());
                model.setParentId(p.getParentId());
                model.setLeaf(!list.stream().anyMatch(m-> StringUtils.equals(m.getParentId(), p.getId())));
                if(!model.getIsLeaf()){
                    model.setChildren(new ArrayList<>());
                }

                return model;
            }).collect(Collectors.toList());
            result.setResult(sysPermissionList);

            result.setSuccess(true);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 获取全部的权限树
     *
     * @return
     */
    @RequestMapping(value = "/queryChannelTreeList", method = RequestMethod.GET)
    public Result<Map<String, Object>> queryChannelTreeList(SaleOrderChannel saleOrderChannel, HttpServletRequest req) {
        Result<Map<String, Object>> result = new Result<>();
        // 全部权限ids
        List<String> ids = new ArrayList<>();
        try {
            QueryWrapper<SaleOrderChannel> query = QueryGenerator.initQueryWrapper(saleOrderChannel, req.getParameterMap());
            query.orderByAsc("create_time");

            List<SaleOrderChannel> list = saleOrderChannelService.list(query);
            for (SaleOrderChannel sysPer : list) {
                ids.add(sysPer.getId());
            }
            List<TreeModel> treeList = new ArrayList<>();
            getTreeModelList(treeList, list, null);

            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("treeList", treeList); // 全部树节点数据
            resMap.put("ids", ids);// 全部树ids
            result.setResult(resMap);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    private void getTreeModelList(List<TreeModel> treeList, List<SaleOrderChannel> metaList, TreeModel temp) {
        for (SaleOrderChannel permission : metaList) {
            String tempPid = permission.getParentId();
            SysPermission model = new SysPermission();
            model.setId(permission.getId());
            model.setName(permission.getName());
            model.setLeaf(!metaList.stream().anyMatch(p-> StringUtils.equals(p.getParentId(), permission.getId())));

            TreeModel tree = new TreeModel(model);
            if (temp == null && oConvertUtils.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            }
        }
    }



}
