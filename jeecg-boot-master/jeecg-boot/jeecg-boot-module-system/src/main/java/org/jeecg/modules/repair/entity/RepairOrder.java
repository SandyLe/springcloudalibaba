package org.jeecg.modules.repair.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.util.Date;

@Data
@TableName("sl_repair_order")
@ApiModel(value = "RepairOrder", description = "维修单")
public class RepairOrder extends CBasicEntity {

    @ApiModelProperty("原单ID")
    private String sourceId;
    @ApiModelProperty("原单Code")
    private String sourceCode;
    @ApiModelProperty("原单类型")
    private Integer sourceBillType;
    @TableField(exist=false)
    private String sourceBillTypeName;
    @ApiModelProperty("单据类型")
    private Integer billType;
    @ApiModelProperty("订单状态")
    private Integer billStatus;
    @ApiModelProperty("订单状态")
    @TableField(exist=false)
    private String billStatusName;
    @ApiModelProperty("单据时间")
    private Date billDate;
    @ApiModelProperty("维修时间")
    private Date operateDate;
    @ApiModelProperty("完工时间")
    private Date finishedDate;
    @ApiModelProperty("状况备注")
    private String operateLog;
    @ApiModelProperty("客户ID")
    private String customerId;
    @ApiModelProperty("客户")
    @TableField(exist=false)
    private String customer;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Integer getSourceBillType() {
        return sourceBillType;
    }

    public void setSourceBillType(Integer sourceBillType) {
        this.sourceBillType = sourceBillType;
    }

    public String getSourceBillTypeName() {

        if (null != sourceBillType) {
            return EnumConvertUtils.getName(BillType.class, sourceBillType);
        }
        return sourceBillTypeName;
    }

    public void setSourceBillTypeName(String sourceBillTypeName) {
        this.sourceBillTypeName = sourceBillTypeName;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getOperateLog() {
        return operateLog;
    }

    public void setOperateLog(String operateLog) {
        this.operateLog = operateLog;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
