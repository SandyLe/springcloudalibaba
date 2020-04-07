package org.jeecg.modules.combind.dto;

import io.swagger.annotations.ApiModel;
import org.jeecg.modules.combind.entity.Assemble;
import org.jeecg.modules.combind.entity.AssembleDtl;

import java.util.List;

@ApiModel(value = "AssembleDto", description = "组装单DTO")
public class AssembleDto extends Assemble {

    private List<AssembleDtl> detaillist;

    public List<AssembleDtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<AssembleDtl> detaillist) {
        this.detaillist = detaillist;
    }
}
