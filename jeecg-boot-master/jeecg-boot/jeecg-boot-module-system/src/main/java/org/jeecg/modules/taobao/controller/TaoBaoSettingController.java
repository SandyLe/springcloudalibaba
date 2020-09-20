package org.jeecg.modules.taobao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.request.TmcUserPermitRequest;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TmcUserPermitResponse;
import com.taobao.api.response.TradesSoldGetResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.TaobaoMessageConstant;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.DeliveryType;
import org.jeecg.common.enums.ReceiptStatus;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.CustomerService;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderChannel;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.service.SaleOrderChannelService;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "淘宝设置")
@RestController
@RequestMapping("/taoBaoSetting")
public class TaoBaoSettingController {


    /**
     * 订阅淘宝消息
     *
     * @return
     */
    @GetMapping(value = "/subscribe")
    @ApiOperation(value = "订阅淘宝消息", notes = "订阅淘宝消息")
    public Result<?> subscribe() throws Exception {

        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "31020098", "b05879570f97a1fbf84d35293c12500b");

        TmcUserPermitRequest req = new TmcUserPermitRequest();
        req.setTopics(String.join(",", TaobaoMessageConstant.TAOBAO_MESSAGE_LIST));
        TmcUserPermitResponse rsp = client.execute(req, "6101107c9971e731a27e8bf4179f36b10341f340637e9c62204151847931");
        System.out.println(rsp.getBody());


        return Result.ok(rsp.getBody());
    }

}
