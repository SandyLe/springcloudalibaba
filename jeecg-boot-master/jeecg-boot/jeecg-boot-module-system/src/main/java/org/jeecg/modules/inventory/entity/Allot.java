package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.modules.basic.entity.CBasicEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("sl_allot")
@ApiModel(value = "Allot", description = "调拨单")
public class Allot extends CBasicEntity {

    @Excel(name = "订单日期",width = 20 , format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("订单日期")
    private Date billdate;
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
    @ApiModelProperty("订单状态")
    private Integer billStatus;
    @ApiModelProperty("订单状态")
    @TableField(exist=false)
    private String billStatusName;

    public Date getBilldate() {
        return billdate;
    }

    public void setBilldate(Date billdate) {
        this.billdate = billdate;
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

    public Integer getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Integer billStatus) {
        this.billStatus = billStatus;
    }

    public void setBillStatusName(String billStatusName) {
        this.billStatusName = billStatusName;
    }

    public String getBillStatusName() {
        if (null != billStatus) {
            return EnumConvertUtils.getName(BillStatus.class, billStatus);
        }
        return billStatusName;
    }
}
