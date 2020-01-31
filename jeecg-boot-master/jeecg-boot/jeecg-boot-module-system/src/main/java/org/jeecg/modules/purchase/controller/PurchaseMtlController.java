package org.jeecg.modules.purchase.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.purchase.entity.PurchaseMtl;
import org.jeecg.modules.purchase.service.PurchaseMtlService;
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
@RequestMapping("/purchaseMtl")
public class PurchaseMtlController extends JeecgController<PurchaseMtl, PurchaseMtlService> {

    @Autowired
    private PurchaseMtlService purchaseMtlService;

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> detailDelete(@RequestParam(name = "id", required = true) String id){
        purchaseMtlService.removeById(id);
        return Result.ok("删除成功!");
    }
}
