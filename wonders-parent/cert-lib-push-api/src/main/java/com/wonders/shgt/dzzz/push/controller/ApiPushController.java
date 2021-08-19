package com.wonders.shgt.dzzz.push.controller;

import com.wonders.web.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wuxx
 * @date 2021/08/18 9:57
 */
@Api(value = "推送", tags = {"推送服务"})
@RestController
@RequestMapping("/certificate-library/push-api")
public class ApiPushController {

    /**
     * 制证接口，数据制证
     * @param json
     * @return
     */
    @ApiOperation(value="自动推送",notes="返回是否成功")
    @PostMapping("/aDoc/change")
    public R changeStatus(String json){
        return R.ok();
    }

    @ApiOperation(value="推送电子证照信息",notes="返回是否成功")
    @PostMapping("/aDoc/push")
    public R push(String json, MultipartFile file) {
        return R.ok();
    }


}
