package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;

@Data
@TableName("sl_inventory_in_mtl")
@ApiModel(value = "InventoryInMtl", description = "入库单明细")
public class InventoryInMtl extends CBasicEntity {

    @ApiModelProperty("入库单ID")
    private String sourceId;
    @ApiModelProperty("原单ID")
    private String sourceBillId;
    @ApiModelProperty("原单类型")
    private Integer sourceBillType;
    @TableField(exist=false)
    private String sourceBillTypeName;

    @ApiModelProperty("产品ID")
    private String mtlId;

    @TableField(exist=false)
    @ApiModelProperty("产品")
    private String material;

    @ApiModelProperty("出库数量")
    private BigDecimal quantity;

    @ApiModelProperty("单位ID")
    private String unitId;

    @TableField(exist=false)
    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("产品Code")
    @TableField(exist=false)
    private String mtlCode;

    @ApiModelProperty("规格")
    @TableField(exist=false)
    private String specification;

    @ApiModelProperty("条形码")
    @TableField(exist=false)
    private String barCode;

    @ApiModelProperty("辅助属性ID")
    private String auxiliaryId;

    public InventoryInMtl() {
    }

    public InventoryInMtl(String sourceId, String sourceBillId, Integer sourceBillType, String mtlId, BigDecimal quantity, String unitId, Integer rowSts, String auxiliaryId) {
        this.sourceId = sourceId;
        this.sourceBillId = sourceBillId;
        this.sourceBillType = sourceBillType;
        this.mtlId = mtlId;
        this.quantity = quantity;
        this.unitId = unitId;
        this.setRowSts(rowSts);
        this.auxiliaryId = auxiliaryId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public String getMtlCode() {
        return mtlCode;
    }

    public void setMtlCode(String mtlCode) {
        this.mtlCode = mtlCode;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSourceBillId() {
        return sourceBillId;
    }

    public void setSourceBillId(String sourceBillId) {
        this.sourceBillId = sourceBillId;
    }

    public Integer getSourceBillType() {
        return sourceBillType;
    }

    public void setSourceBillType(Integer sourceBillType) {
        this.sourceBillType = sourceBillType;
    }

    public String getSourceBillTypeName() {
        return sourceBillTypeName;
    }

    public void setSourceBillTypeName(String sourceBillTypeName) {
        this.sourceBillTypeName = sourceBillTypeName;
    }

    public String getAuxiliaryId() {
        return auxiliaryId;
    }

    public void setAuxiliaryId(String auxiliaryId) {
        this.auxiliaryId = auxiliaryId;
    }
}
