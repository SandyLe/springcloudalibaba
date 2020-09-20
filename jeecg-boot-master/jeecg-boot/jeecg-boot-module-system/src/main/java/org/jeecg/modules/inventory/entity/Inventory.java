package org.jeecg.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;

@Data
@TableName("sl_inventory")
public class Inventory extends CBasicEntity {

    @ApiModelProperty("产品ID")
    private String mtlId;
    @TableField(exist=false)
    @ApiModelProperty("产品")
    private String material;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库")
    @TableField(exist=false)
    private String warehouse;
    @ApiModelProperty("库存")
    private BigDecimal stockAmount;
    @ApiModelProperty("库存下限")
    private BigDecimal warningstart;
    @ApiModelProperty("库存上线")
    private BigDecimal warningend;
    @ApiModelProperty("单位ID")
    private String unitId;
    @TableField(exist=false)
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("辅助属性ID")
    private String auxiliaryId;
    @ApiModelProperty("辅助属性中文值")
    @TableField(exist=false)
    private String suppValueMap;

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

    public BigDecimal getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(BigDecimal stockAmount) {
        this.stockAmount = stockAmount;
    }

    public BigDecimal getWarningstart() {
        return warningstart;
    }

    public void setWarningstart(BigDecimal warningstart) {
        this.warningstart = warningstart;
    }

    public BigDecimal getWarningend() {
        return warningend;
    }

    public void setWarningend(BigDecimal warningend) {
        this.warningend = warningend;
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

    public String getAuxiliaryId() {
        return auxiliaryId;
    }

    public void setAuxiliaryId(String auxiliaryId) {
        this.auxiliaryId = auxiliaryId;
    }

    public String getSuppValueMap() {
        return suppValueMap;
    }

    public void setSuppValueMap(String suppValueMap) {
        this.suppValueMap = suppValueMap;
    }
}
