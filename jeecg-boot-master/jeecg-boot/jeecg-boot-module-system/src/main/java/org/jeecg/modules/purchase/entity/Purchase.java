package org.jeecg.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 采购列表
 * @Author: tomkluas
 * @Date: 2019/12/27 15:24
 * @Version: V1.0
 */
@TableName("sl_purchase")
@ApiModel(value = "Purchase", description = "采购订单")
public class Purchase extends BasicEntity {

    /*供应商*/
    @Excel(name = "供应商", width = 15)
    @ApiModelProperty("供应商")
    private String vendorId;

    /*仓库id*/
    @Excel(name = "仓库id", width = 15)
    @ApiModelProperty("仓库id")
    private String warehouseId;

    /*结算账户*/
    @Excel(name = "结算账户",width = 20)
    @ApiModelProperty("结算账户")
    private String account;

    /*实付金额*/
    @Excel(name = "实付金额",width = 20)
    @ApiModelProperty("实付金额")
    private String payamount;

    /*订单日期*/
    @Excel(name = "订单日期",width = 20 , format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("订单日期")
    private Date billdate;

    /*总金额*/
    @Excel(name = "总金额",width = 20)
    @ApiModelProperty("总金额")
    private String totalamount;

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public Date getBilldate() {
        return billdate;
    }

    public void setBilldate(Date billdate) {
        this.billdate = billdate;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }


    /*入库单*/
    @ApiModelProperty("入库单")
    @TableField(exist = false)
    private InventoryIn inventoryin;

    public InventoryIn getInventoryin() {
        return inventoryin;
    }

    public void setInventoryin(InventoryIn inventoryin) {
        this.inventoryin = inventoryin;
    }
    /*出库单*/
    @ApiModelProperty("出库单")
    @TableField(exist = false)
    private InventoryOut inventoryOut;
    public InventoryOut getInventoryOut() {
        return inventoryOut;
    }
    public void setInventoryOut(InventoryOut inventoryOut) {
        this.inventoryOut = inventoryOut;
    }
}
