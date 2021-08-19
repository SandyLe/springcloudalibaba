package com.wonders.shgt.dzzz.signature.controller;

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
@Api(value = "签章", tags = {"签章服务"})
@RestController
@RequestMapping("/certificate-library/seal-api")
public class ApiSealController {

    /**
     * 页面组织签章
     * @return
     */
    @ApiOperation(value="页面组织签章",notes="返回签章信息")
    @PostMapping("/seal/page/company")
    public R sealPageCompany(String contact,String sealIds, String sourceCode, String businessCode, MultipartFile file){
        return R.ok();
    }
    /**
     * 页面个人签章
     * @return
     */
    @ApiOperation(value="页面个人签章",notes="返回签章信息")
    @PostMapping("/seal/page/personal")
    public R sealPagePersonal(String contact, String personName, String sourceCode, String businessCode, MultipartFile file){
        return R.ok();
    }

    /**
     * 验签
     * @return
     */
    @ApiOperation(value="验签",notes="返回验签信息")
    @PostMapping("/seal/pdfverifier")
    public R pdfVerifier(MultipartFile file){
        return R.ok();
    }

    /**
     * 验签
     * @return
     */
    @ApiOperation(value="批量验签",notes="返回验签信息")
    @PostMapping("/seal/pdfVerifiers")
    public R pdfVerifiers(MultipartFile file){
        return R.ok();
    }



    /**
     * 同步组织签章
     * @return
     */
    @ApiOperation(value="同步组织签章",notes="返回签章文件")
    @PostMapping("/seal/sync/company")
    public R sealSyncCompany(String sealData, MultipartFile file){
        return R.ok();
    }

    /**
     * 同步个人签章
     * @return
     */
    @ApiOperation(value="同步个人签章",notes="返回签章文件")
    @PostMapping("/seal/sync/personal")
    public R sealSyncPersonal(String contact, String personName, MultipartFile file, String sealData){
        return R.ok();
    }

}
