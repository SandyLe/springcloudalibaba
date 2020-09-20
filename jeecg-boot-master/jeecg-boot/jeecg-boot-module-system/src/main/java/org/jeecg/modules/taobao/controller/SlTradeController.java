package org.jeecg.modules.taobao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.taobao.api.domain.Trade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.taobao.entity.SlTrade;
import org.jeecg.modules.taobao.service.SlTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Api(tags = "淘宝订单")
@RestController
@RequestMapping("/slTrade")
public class SlTradeController {

    @Autowired
    private SlTradeService slTradeService;

    /**
     * 分页列表查询
     *
     * @param slTrade
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取淘宝订单数据列表", notes = "获取所有淘宝订单数据列表")
    @GetMapping(value = "/getPage")
//    @PermissionData
    public Result<?> list(SlTrade slTrade, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<SlTrade> queryWrapper = QueryGenerator.initQueryWrapper(slTrade, req.getParameterMap());
        Page<SlTrade> page = new Page<SlTrade>(pageNo, pageSize);

        IPage<SlTrade> pageList = slTradeService.page(page, queryWrapper);

        List<SlTrade> slTradeList = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(slTradeList)) {
            slTradeList.stream().forEach(o->{
                o.setChildren(Lists.newArrayList());
            });
            pageList.setRecords(slTradeList);
        }

        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }
    /**
     * 查询订单详细信息
     *
     * @return
     */
    @GetMapping(value = "/queryFromTaoBao")
    @ApiOperation(value = "订阅淘宝消息", notes = "订阅淘宝消息")
    public Result<?> queryFromTaoBao() throws Exception {

        slTradeService.queryTradeFromTaoBao(1188596352219058652l);


        return Result.ok();
    }

    /**
     * 获取订单信息
     *
     * @param slTrade
     * @return
     */
    @PostMapping(value = "/getList")
    @AutoLog(value = "修改淘宝订单")
    @ApiOperation(value = "修改淘宝订单", notes = "修改淘宝订单")
    public Result<?> getList(@RequestBody SlTrade slTrade){

        System.out.println("Go open taobao ************");
        System.out.println(slTrade.getBuyerNick());
        SlTrade existed = new SlTrade();
        existed.setBuyerNick("BuyerNick: "+slTrade.getBuyerNick());
        existed.setAdjustFee("1314520");
        existed.setMarkDesc("Sandylee test");

        return Result.ok(Lists.newArrayList(existed));
    }

    /**
     * 保存
     *
     * @param trade
     * @return
     */
    @PostMapping(value = "/save")
    @AutoLog(value = "保存地址")
    @ApiOperation(value = "保存地址", notes = "保存地址")
    public Result<?> add(@RequestBody Trade trade) throws Exception {

        Trade dbTrade = slTradeService.saveOrUpdateTrade(trade, "save");
        return Result.ok("保存成功！");
    }
}
