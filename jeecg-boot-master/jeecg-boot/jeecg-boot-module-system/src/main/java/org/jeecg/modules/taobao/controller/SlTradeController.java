package org.jeecg.modules.taobao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TmcUserPermitRequest;
import com.taobao.api.response.TmcUserPermitResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.taobao.entity.SlTrade;
import org.jeecg.modules.taobao.service.SlTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "淘宝订单")
@RestController
@RequestMapping("/slTrade")
public class SlTradeController {

    @Autowired
    private SlTradeService slTradeService;

    /**
     * 查询订单详细信息
     *
     * @return
     */
    @GetMapping(value = "/queryFromTaoBao")
    @ApiOperation(value = "订阅淘宝消息", notes = "订阅淘宝消息")
    public Result<?> queryFromTaoBao() throws Exception {

        slTradeService.queryAndSave(1188596352219058652l);


        return Result.ok();
    }

    /**
     * 获取订单信息
     *
     * @param saleOrder
     * @return
     */
    @PostMapping(value = "/getList")
    @AutoLog(value = "修改销售订单")
    @ApiOperation(value = "修改销售订单", notes = "修改销售订单")
    public Result<?> getList(@RequestBody SlTrade saleOrder){

        System.out.println("Go open taobao ************");
        SlTrade existed = new SlTrade();
        existed.setAdjustFee("1314520");
        existed.setMarkDesc("Sandylee test");

        return Result.ok(Lists.newArrayList(existed));
    }
}
