package com.wonders.shgt.dzzz.usage.controller;

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
@Api(value = "用证", tags = {"用证服务"})
@RestController
@RequestMapping("/certificate-library/usage-api")
public class ApiUsageController {

    /**
     * 制证接口，数据制证
     * @param certId
     * @return
     */
    @ApiOperation(value="文书详情查询",notes="返回文书信息")
    @PostMapping("/aDoc/{aDocId}")
    public R aDocInfo(String certId){
        return R.ok();
    }

    @ApiOperation(value="文书列表查询",notes="返回文书列表")
    @PostMapping("/aDoc/list")
    public R aDocList(String aDocId, String sourceCode, String businessCode, String aDocNo, String licenseName, String aDocName, String acName, Integer page, Integer limit) {
        return R.ok();
    }

    @ApiOperation(value="附件下载",notes="返回文件")
    @PostMapping("/attachment/download")
    public R attachment(String attacId) {
        return R.ok();
    }

    @ApiOperation(value="文书下载",notes="返回文件")
    @PostMapping("/aDoc/download")
    public R aDocFile(String aDocId) {
        return R.ok();
    }
}
