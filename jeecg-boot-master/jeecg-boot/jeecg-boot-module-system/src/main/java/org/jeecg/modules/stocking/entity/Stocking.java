package org.jeecg.modules.stocking.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;

import java.math.BigDecimal;

@Data
@TableName("sl_stocking")
@ApiModel(value = "Stocking", description = "盘点单")
public class Stocking extends BasicEntity {

    @ApiModelProperty("产品ID")
    private String mtlId;
    @TableField(exist=false)
    @ApiModelProperty("产品")
    private String material;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库")
    @TableField(exist=false)
    private String warehouse;
    @ApiModelProperty("盘点数量")
    private BigDecimal stockAmount;
    @ApiModelProperty("盘点前数量")
    private BigDecimal beforeAmount;
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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(BigDecimal stockAmount) {
        this.stockAmount = stockAmount;
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

    public BigDecimal getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(BigDecimal beforeAmount) {
        this.beforeAmount = beforeAmount;
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
