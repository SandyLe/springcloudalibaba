package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("sl_inventory_in")
@ApiModel(value = "InventoryIn", description = "入库单")
public class InventoryIn extends BasicEntity {

    @ApiModelProperty("原单ID：销售退货，采购入库")
    private String sourceId;
    @ApiModelProperty("原单Code：销售出库，采购退货Code")
    private String sourceCode;
    @ApiModelProperty("原单类型")
    private Integer sourceBillType;
    @TableField(exist=false)
    private String sourceBillTypeName;
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
    @TableField(exist=false)
    private String billStatusName;

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

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getBillStatusName() {
        if (null != billStatus) {
            return EnumConvertUtils.getName(BillStatus.class, billStatus);
        }
        return null;
    }

    public void setBillStatusName(String billStatusName) {
        this.billStatusName = billStatusName;
    }

    public String getSourceBillTypeName() {
        if (null != sourceBillType) {
            return EnumConvertUtils.getName(BillType.class, sourceBillType);
        }
        return null;
    }

    public void setSourceBillTypeName(String sourceBillTypeName) {
        this.sourceBillTypeName = sourceBillTypeName;
    }

}
