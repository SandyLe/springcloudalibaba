package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sl_inventory_in")
@ApiModel(value = "InventoryIn", description = "入库单")
public class InventoryIn extends BasicEntity {

    @ApiModelProperty("原单ID：销售退货，采购入库")
    private String sourceId;
    @ApiModelProperty("原单类型")
    private Integer billType;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库")
    @TableField(exist=false)
    private String warehouse;
    @ApiModelProperty("入库时间")
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
}
