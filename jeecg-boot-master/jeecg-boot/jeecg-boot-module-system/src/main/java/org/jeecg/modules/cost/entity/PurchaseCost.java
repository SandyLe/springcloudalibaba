package org.jeecg.modules.cost.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;

@Data
@TableName("sl_purchase_cost")
@ApiModel(value = "PurchaseCost", description = "批次采购平均成本")
public class PurchaseCost extends CBasicEntity {

    @ApiModelProperty("产品ID")
    private String mtlId;
    @TableField(exist=false)
    @ApiModelProperty("产品")
    private String material;
    @ApiModelProperty("平均价格")
    private BigDecimal averagePrice;
    @ApiModelProperty("单位ID")
    private String unitId;
    @TableField(exist=false)
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("批次号")
    private String batchNo;

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

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
