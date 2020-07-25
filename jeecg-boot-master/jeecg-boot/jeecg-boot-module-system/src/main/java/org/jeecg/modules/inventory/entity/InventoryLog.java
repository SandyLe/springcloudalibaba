package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sl_inventory_log")
public class InventoryLog extends CBasicEntity {

    @ApiModelProperty("产品ID")
    private String mtlId;
    @ApiModelProperty("操作单ID")
    private String sourceId;
    @ApiModelProperty("单据ID")
    private String sourceBillId;
    @ApiModelProperty("单据类型ID")
    private Integer sourceBillType;
    @TableField(exist = false)
    @ApiModelProperty("产品")
    private String material;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库")
    @TableField(exist = false)
    private String warehouse;
    @ApiModelProperty("库存")
    private BigDecimal stockAmount;
    @ApiModelProperty("操作数量")
    private BigDecimal optAmount;
    @ApiModelProperty("操作前数量")
    private BigDecimal beforeAmount;
    @ApiModelProperty("单位ID")
    private String unitId;
    @TableField(exist = false)
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("操作ID")
    private Integer operationId;
    @TableField(exist = false)
    @ApiModelProperty("操作")
    private String operation;
    @ApiModelProperty("批次号")
    private String batchNo;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date operateTime;

    public InventoryLog() {
    }

    public InventoryLog(String sourceId, String sourceBillId, Integer sourceBillType, String mtlId, String warehouseId, BigDecimal stockAmount, BigDecimal optAmount, BigDecimal beforeAmount, String unitId, Integer operationId, String batchNo, String companyId) {
        this.mtlId = mtlId;
        this.sourceId = sourceId;
        this.sourceBillId = sourceBillId;
        this.sourceBillType = sourceBillType;
        this.warehouseId = warehouseId;
        this.stockAmount = stockAmount;
        this.optAmount = optAmount;
        this.beforeAmount = beforeAmount;
        this.unitId = unitId;
        this.operationId = operationId;
        this.batchNo = batchNo;
        this.setCompanyId(companyId);
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSourceBillId() {
        return sourceBillId;
    }

    public void setSourceBillId(String sourceBillId) {
        this.sourceBillId = sourceBillId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
