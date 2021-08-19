package com.wonders.shgt.dzzz.make.controller;

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
@Api(value = "制证", tags = {"制证服务"})
@RestController
@RequestMapping("/certificate-library/pdf-api")
public class ApiGenPdfController {

    /**
     * 制证接口，数据制证
     * @param sourceCode
     * @param businessCode
     * @param data
     * @return
     */
    @ApiOperation(value="制证",notes="结构化数据制证接口")
    @PostMapping("/gen-pdf")
    public R genCert(String sourceCode,String businessCode,String data){
        return R.ok();
    }

    @ApiOperation(value="word转pdf",notes="Word文件制证接口")
    @PostMapping("/word-pdf")
    public R genCert(String sourceCode,String businessCode, MultipartFile file) {
        return R.ok();
    }
}
