package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@TableName("sl_material_price")
@ApiModel(value = "MaterialPrice", description = "产品价格")
public class MaterialPrice extends BasicEntity {

    @ApiModelProperty("产品ID")
    private String materialId;
    @ApiModelProperty("产品ID")
    @TableField(exist=false)
    private String material;

    @ApiModelProperty("客户类型ID")
    private String customerTypeId;
    @ApiModelProperty("客户类型")
    @TableField(exist=false)
    private String customerType;

    @ApiModelProperty("单位ID")
    private String unitId;
    @ApiModelProperty("产品")
    @TableField(exist=false)
    private String unit;

    @ApiModelProperty("价格")
    private BigDecimal price;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(String customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
