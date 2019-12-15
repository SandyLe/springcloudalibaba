package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@TableName("sl_material_self_unit")
@ApiModel(value = "MaterialSelfUnit", description = "产品单位")
public class MaterialSelfUnit extends BasicEntity {

    @ApiModelProperty("产品ID")
    private String sourceId;

    @ApiModelProperty("单位ID")
    private String unitId;

    @ApiModelProperty("单位")
    @TableField(exist=false)
    private String unit;

    @ApiModelProperty("单位类型:0-主单位，1-附属单位，数据字典")
    private String unitType;

    @ApiModelProperty("与主单位兑换比例")
    private BigDecimal qty;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
