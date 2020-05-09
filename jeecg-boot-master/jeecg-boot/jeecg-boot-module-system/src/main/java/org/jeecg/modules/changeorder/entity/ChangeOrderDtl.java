package org.jeecg.modules.changeorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;

@Data
@TableName("sl_change_order_dtl")
@ApiModel(value = "ChangeOrderDtl", description = "换货单明细")
public class ChangeOrderDtl extends CBasicEntity {

    @ApiModelProperty("原单id")
    private String sourceId;
    @ApiModelProperty("产品ID")
    private String mtlId;
    @ApiModelProperty("产品ID")
    private String newMtlId;
    @TableField(exist=false)
    @ApiModelProperty("产品")
    private String material;
    @ApiModelProperty("单位ID")
    private String unitId;
    @TableField(exist=false)
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("数量")
    private BigDecimal quantity;
    @TableField(exist=false)
    @ApiModelProperty("产品")
    private String newMaterial;
    @ApiModelProperty("单位ID")
    private String newUnitId;
    @TableField(exist=false)
    @ApiModelProperty("单位")
    private String newUnit;
    @ApiModelProperty("数量")
    private BigDecimal newQuantity;

    public String getMtlId() {
        return mtlId;
    }

    public void setMtlId(String mtlId) {
        this.mtlId = mtlId;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getNewMtlId() {
        return newMtlId;
    }

    public void setNewMtlId(String newMtlId) {
        this.newMtlId = newMtlId;
    }

    public String getNewMaterial() {
        return newMaterial;
    }

    public void setNewMaterial(String newMaterial) {
        this.newMaterial = newMaterial;
    }

    public String getNewUnitId() {
        return newUnitId;
    }

    public void setNewUnitId(String newUnitId) {
        this.newUnitId = newUnitId;
    }

    public String getNewUnit() {
        return newUnit;
    }

    public void setNewUnit(String newUnit) {
        this.newUnit = newUnit;
    }

    public BigDecimal getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(BigDecimal newQuantity) {
        this.newQuantity = newQuantity;
    }
}
