package org.jeecg.modules.changeorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.changeorder.dto.ChangeOrderDto;
import org.jeecg.modules.changeorder.entity.ChangeOrder;
import org.jeecg.modules.changeorder.service.ChangeOrderDtlService;
import org.jeecg.modules.changeorder.service.ChangeOrderService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "换货单列表")
@RestController
@RequestMapping("/changeOrder")
public class ChangeOrderController extends JeecgController<ChangeOrder, ChangeOrderService> {
    @Autowired
    private ChangeOrderService changeOrderService;
    @Autowired
    private ChangeOrderDtlService changeOrderDtlService;
    @Autowired
    private ISysUserService iSysUserService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(ChangeOrder changeOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ChangeOrder> queryWrapper = QueryGenerator.initQueryWrapper(changeOrder, req.getParameterMap());
        Page<ChangeOrder> page = new Page<>(pageNo, pageSize);
        IPage<ChangeOrder> pageList = changeOrderService.page(page, queryWrapper);
        List<ChangeOrder> datas = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(datas)) {
            Map<String, String> userMap = new HashMap<>();

            List<String> userIds = datas.stream().map(ChangeOrder::getOperateUserId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(userIds)) {
                Collection<SysUser> sysUsers = iSysUserService.listByIds(userIds);
                userMap.putAll(sysUsers.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getRealname)));
            }
            datas.stream().forEach(o -> {
                o.setOperateUserName(userMap.get(o.getOperateUserId()));
            });
        }
        pageList.setRecords(datas);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody ChangeOrderDto changeOrderdto) {
        changeOrderService.saveOrder(changeOrderdto);
        return Result.ok();
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody ChangeOrderDto changeOrderdto) {
        changeOrderService.editOrder(changeOrderdto);
        return Result.ok();
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        changeOrderService.removeById(id);
        changeOrderDtlService.deleteBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        changeOrderService.removeByIds(Arrays.asList(ids.split(",")));
        changeOrderDtlService.deleteBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/getOne")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ChangeOrder changeOrder = changeOrderService.getById(id);
        if (changeOrder == null) {
            return Result.ok("未找到对应数据");
        }
        ChangeOrderDto changeOrderdtldto = new ChangeOrderDto();
        BeanUtils.copyProperties(changeOrder, changeOrderdtldto);
        changeOrderdtldto.setDetaillist(changeOrderDtlService.findBySourceId(changeOrder.getId()));

        return Result.ok(changeOrderdtldto);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code) {

        LambdaQueryWrapper<ChangeOrder> lambdaQueryWrapper = new LambdaQueryWrapper<ChangeOrder>().eq(ChangeOrder::getCode, code);
        ChangeOrder changeOrder = changeOrderService.getOne(lambdaQueryWrapper);
        if (changeOrder == null) {
            return Result.ok("未找到对应数据");
        }
        return Result.ok(changeOrder);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ChangeOrder changeOrder) {
        return super.exportXls(request, changeOrder, ChangeOrder.class, "拆卸单列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ChangeOrder.class);
    }
}
