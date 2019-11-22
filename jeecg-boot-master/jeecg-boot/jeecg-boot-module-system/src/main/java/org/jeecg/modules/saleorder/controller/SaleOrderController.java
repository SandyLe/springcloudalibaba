package org.jeecg.modules.saleorder.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "单表DEMO")
@RestController
@RequestMapping("/saleOrder")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;
    /**
     * 添加
     *
     * @param saleOrder
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加测试DEMO")
    @ApiOperation(value = "添加DEMO", notes = "添加DEMO")
    public Result<?> add(@RequestBody SaleOrder saleOrder) {
        saleOrderService.save(saleOrder);
        return Result.ok("添加成功！");
    }
}
