package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.basic.entity.BasicEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sl_inventory_out")
@ApiModel(value = "InventoryOut", description = "出库单")
public class InventoryOut extends BasicEntity {

    @ApiModelProperty("原单ID：销售出库，采购退货")
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
    @ApiModelProperty("出货时间")
    private String putOutTime;
    @ApiModelProperty("订单状态")
    private Integer billStatus;

//    public InventoryOut() {
//    }
//
//    public InventoryOut(String sourceId, Integer billType, Integer sourceBillType, String warehouseId, Date putOutTime, Integer billStatus) {
//        this.sourceId = sourceId;
//        this.billType = billType;
//        this.sourceBillType = sourceBillType;
//        this.warehouseId = warehouseId;
//        this.putOutTime = DateUtils.formatTime(putOutTime);
//        this.billStatus = billStatus;
//    }

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

    public String getPutOutTime() {
        return putOutTime;
    }

    public void setPutOutTime(String putOutTime) {
        this.putOutTime = putOutTime;
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
}
