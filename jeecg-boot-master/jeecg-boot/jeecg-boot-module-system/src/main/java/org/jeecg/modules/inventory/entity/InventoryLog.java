package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;

import java.math.BigDecimal;

@Data
@TableName("sl_inventory_log")
public class InventoryLog extends BasicEntity {

    @ApiModelProperty("产品ID")
    private String mtlId;
    @ApiModelProperty("原单ID")
    private String sourceId;
    @ApiModelProperty("原单类型ID")
    private Integer sourceBillType;
    @TableField(exist=false)
    @ApiModelProperty("产品")
    private String material;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库")
    @TableField(exist=false)
    private String warehouse;
    @ApiModelProperty("库存")
    private BigDecimal stockAmount;
    @ApiModelProperty("操作数量")
    private BigDecimal optAmount;
    @ApiModelProperty("操作前数量")
    private BigDecimal beforeAmount;
    @ApiModelProperty("单位ID")
    private String unitId;
    @TableField(exist=false)
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("操作ID")
    private Integer operationId;
    @TableField(exist=false)
    @ApiModelProperty("操作")
    private String operation;

    public InventoryLog() {
    }

    public InventoryLog(String sourceId, Integer sourceBillType, String mtlId, String warehouseId, BigDecimal stockAmount, BigDecimal optAmount, BigDecimal beforeAmount, String unitId, Integer operationId) {
        this.mtlId = mtlId;
        this.sourceId = sourceId;
        this.sourceBillType = sourceBillType;
        this.warehouseId = warehouseId;
        this.stockAmount = stockAmount;
        this.optAmount = optAmount;
        this.beforeAmount = beforeAmount;
        this.unitId = unitId;
        this.operationId = operationId;
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

    public BigDecimal getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(BigDecimal beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getSourceBillType() {
        return sourceBillType;
    }

    public void setSourceBillType(Integer sourceBillType) {
        this.sourceBillType = sourceBillType;
    }

    public BigDecimal getOptAmount() {
        return optAmount;
    }

    public void setOptAmount(BigDecimal optAmount) {
        this.optAmount = optAmount;
    }
}
