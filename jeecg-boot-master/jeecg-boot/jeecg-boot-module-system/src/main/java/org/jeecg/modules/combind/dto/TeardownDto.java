package org.jeecg.modules.combind.dto;

import io.swagger.annotations.ApiModel;
import org.jeecg.modules.combind.entity.Teardown;
import org.jeecg.modules.combind.entity.TeardownDtl;

import java.util.List;

@ApiModel(value = "TeardownDto", description = "拆卸单DTO")
public class TeardownDto extends Teardown {

    private List<TeardownDtl> detaillist;

    public List<TeardownDtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<TeardownDtl> detaillist) {
        this.detaillist = detaillist;
    }
}
