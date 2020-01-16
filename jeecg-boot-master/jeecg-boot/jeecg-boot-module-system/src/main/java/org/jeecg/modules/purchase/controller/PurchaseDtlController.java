package org.jeecg.modules.purchase.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.purchase.entity.PurchaseDtl;
import org.jeecg.modules.purchase.service.IPurchaseDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/14 14:32
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "采购列表")
@RestController
@RequestMapping("/purchasedtl")
public class PurchaseDtlController extends JeecgController<PurchaseDtl, IPurchaseDtlService> {

    @Autowired
    private IPurchaseDtlService purchasedtlService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        purchasedtlService.removeById(id);
        return Result.ok("删除成功!");
    }
}
