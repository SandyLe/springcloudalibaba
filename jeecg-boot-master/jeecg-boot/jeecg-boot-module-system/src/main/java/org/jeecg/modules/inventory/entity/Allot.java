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
@TableName("sl_allot")
@ApiModel(value = "Allot", description = "调拨单")
public class Allot extends BasicEntity {

    @ApiModelProperty("单据时间")
    private Date billDate;
    @ApiModelProperty("调出仓库ID")
    private String fromWarehouseId;
    @ApiModelProperty("调出仓库")
    @TableField(exist=false)
    private String fromWarehouse;
    @ApiModelProperty("调入仓库ID")
    private String toWarehouseId;
    @ApiModelProperty("调入仓库")
    @TableField(exist=false)
    private String toWarehouse;

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getFromWarehouseId() {
        return fromWarehouseId;
    }

    public void setFromWarehouseId(String fromWarehouseId) {
        this.fromWarehouseId = fromWarehouseId;
    }

    public String getFromWarehouse() {
        return fromWarehouse;
    }

    public void setFromWarehouse(String fromWarehouse) {
        this.fromWarehouse = fromWarehouse;
    }

    public String getToWarehouseId() {
        return toWarehouseId;
    }

    public void setToWarehouseId(String toWarehouseId) {
        this.toWarehouseId = toWarehouseId;
    }

    public String getToWarehouse() {
        return toWarehouse;
    }

    public void setToWarehouse(String toWarehouse) {
        this.toWarehouse = toWarehouse;
    }
}
