package org.jeecg.modules.purchase.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.PurchaseMtl;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class PurchaseDtlDto extends Purchase {

    @ApiModelProperty("入库时间")
    @TableField(exist=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date putInTime;

    @ApiModelProperty("入库单ID")
    @TableField(exist =  false)
    private InventoryIn inventoryIn;

    private List<PurchaseMtl> detaillist;

    public List<PurchaseMtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<PurchaseMtl> detaillist) {
        this.detaillist = detaillist;
    }

    public Date getPutInTime() {
        return putInTime;
    }

    public void setPutInTime(Date putInTime) {
        this.putInTime = putInTime;
    }

    public InventoryIn getInventoryIn() {
        return inventoryIn;
    }

    public void setInventoryIn(InventoryIn inventoryIn) {
        this.inventoryIn = inventoryIn;
    }
}
