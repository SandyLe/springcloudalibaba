package org.jeecg.modules.taobao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.taobao.entity.SlTrade;
import org.jeecg.modules.taobao.entity.SlTradeOrder;
import org.jeecg.modules.taobao.service.SlTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Api(tags = "淘宝订单子表")
@RestController
@RequestMapping("/slTradeOrder")
public class SlTradeOrderController {

    @Autowired
    private SlTradeOrderService slTradeOrderService;

    /**
     * 分页列表查询
     *
     * @param slTradeOrder
     * @param req
     * @return
     */
    @ApiOperation(value = "获取淘宝订单数据列表", notes = "获取所有淘宝订单数据列表")
    @GetMapping(value = "/list")
//    @PermissionData
    public Result<?> list(SlTradeOrder slTradeOrder, HttpServletRequest req) {
        QueryWrapper<SlTradeOrder> queryWrapper = QueryGenerator.initQueryWrapper(slTradeOrder, req.getParameterMap());
        List<SlTradeOrder> orders = slTradeOrderService.list(queryWrapper);
        return Result.ok(orders);
    }

}
