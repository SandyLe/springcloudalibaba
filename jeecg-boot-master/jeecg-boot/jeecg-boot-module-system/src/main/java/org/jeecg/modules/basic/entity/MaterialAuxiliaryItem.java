package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_material_auxiliary_item")
@ApiModel(value = "MaterialAuxiliaryItem", description = "产品辅助属性条目")
public class MaterialAuxiliaryItem extends BaseBill {

    @ApiModelProperty("辅助属性Code值")
    private String suppCode;
    @ApiModelProperty("辅助属性Name值")
    private String suppName;

    @ApiModelProperty("辅助属性值Code值")
    private String suppValueCode;
    @ApiModelProperty("辅助属性值name值")
    private String suppValueName;

    @ApiModelProperty("所属对象")
    private String sourceId;

    @ApiModelProperty("产品ID")
    private String mtlId;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSuppCode() {
        return suppCode;
    }

    public void setSuppCode(String suppCode) {
        this.suppCode = suppCode;
    }

    public String getSuppName() {
        return suppName;
    }

    public void setSuppName(String suppName) {
        this.suppName = suppName;
    }

    public String getSuppValueCode() {
        return suppValueCode;
    }

    public void setSuppValueCode(String suppValueCode) {
        this.suppValueCode = suppValueCode;
    }

    public String getSuppValueName() {
        return suppValueName;
    }

    public void setSuppValueName(String suppValueName) {
        this.suppValueName = suppValueName;
    }

    public String getMtlId() {
        return mtlId;
    }

    public void setMtlId(String mtlId) {
        this.mtlId = mtlId;
    }
}
