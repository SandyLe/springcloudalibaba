package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_material_auxiliary")
@ApiModel(value = "MaterialAuxiliary", description = "产品辅助属性")
public class MaterialAuxiliary extends BaseBill {

    @ApiModelProperty("辅助属性Code值")
    private String suppCodeMap;

    @ApiModelProperty("辅助属性中文值")
    private String suppValueMap;

    @ApiModelProperty("所属对象")
    private String sourceId;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSuppCodeMap() {
        return suppCodeMap;
    }

    public void setSuppCodeMap(String suppCodeMap) {
        this.suppCodeMap = suppCodeMap;
    }

    public String getSuppValueMap() {
        return suppValueMap;
    }

    public void setSuppValueMap(String suppValueMap) {
        this.suppValueMap = suppValueMap;
    }
}
