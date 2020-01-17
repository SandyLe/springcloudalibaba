package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;

import java.math.BigDecimal;

@Data
@TableName("sl_inventory_out_mtl")
@ApiModel(value = "InventoryOutMtl", description = "出库单明细")
public class InventoryOutMtl extends BasicEntity {

    @ApiModelProperty("出库单ID")
    private String sourceId;
    @ApiModelProperty("产品ID")
    private String mtlId;
    @ApiModelProperty("出库数量")
    private String quantity;
    @ApiModelProperty("单位ID")
    private String unitId;

    @TableField(exist=false)
    @ApiModelProperty("产品")
    private String material;
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

    public InventoryOutMtl() {
    }

    public InventoryOutMtl(String sourceId, String mtlId, String quantity, String unitId) {
        this.sourceId = sourceId;
        this.mtlId = mtlId;
        this.quantity = quantity;
        this.unitId = unitId;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
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
}
