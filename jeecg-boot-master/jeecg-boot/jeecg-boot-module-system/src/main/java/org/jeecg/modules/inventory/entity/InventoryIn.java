package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("sl_inventory_in")
@ApiModel(value = "InventoryIn", description = "入库单")
public class InventoryIn extends BasicEntity {

    @ApiModelProperty("原单ID：销售退货，采购入库")
    private String sourceId;
    @ApiModelProperty("原单类型")
    private Integer sourceBillType;
    @ApiModelProperty("单据类型")
    private Integer billType;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库")
    @TableField(exist=false)
    private String warehouse;

    @ApiModelProperty("入库时间")
    @Excel(name = "入库时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date putInTime;

    @ApiModelProperty("订单状态")
    private Integer billStatus;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
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

    public Date getPutInTime() {
        return putInTime;
    }

    public void setPutInTime(Date putInTime) {
        this.putInTime = putInTime;
    }

    public Integer getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Integer billStatus) {
        this.billStatus = billStatus;
    }

    public Integer getSourceBillType() {
        return sourceBillType;
    }

    public void setSourceBillType(Integer sourceBillType) {
        this.sourceBillType = sourceBillType;
    }
	
	@ApiModelProperty("入库单商品详情")
    @TableField(exist=false)
    private List<InventoryInMtl> inventoryInMtls;

    public List<InventoryInMtl> getInventoryInMtls() {
        return inventoryInMtls;
    }

    public void setInventoryInMtls(List<InventoryInMtl> inventoryInMtls) {
        this.inventoryInMtls = inventoryInMtls;
    }}
