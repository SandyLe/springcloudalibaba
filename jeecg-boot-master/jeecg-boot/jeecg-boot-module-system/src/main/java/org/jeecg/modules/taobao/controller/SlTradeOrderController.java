package org.jeecg.modules.taobao.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.taobao.service.SlTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "淘宝订单")
@RestController
@RequestMapping("/slTradeOrder")
public class SlTradeOrderController {

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

}
