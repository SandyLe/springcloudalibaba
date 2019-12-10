package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@TableName("sl_material")
@ApiModel(value = "Material", description = "产品")
public class Material extends BasicEntity {

    @ApiModelProperty("规格")
    private String specification;
    @ApiModelProperty("品牌Id")
    private String brandId;
    @ApiModelProperty("品牌")
    @TableField(exist=false)
    private String brand;
    @ApiModelProperty("类型Id")
    private String typeId;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("库存上限")
    private BigDecimal uplimit;
    @ApiModelProperty("库存下限")
    private BigDecimal downlimit;
    @ApiModelProperty("图片")
    private String pictures;

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getUplimit() {
        return uplimit;
    }

    public void setUplimit(BigDecimal uplimit) {
        this.uplimit = uplimit;
    }

    public BigDecimal getDownlimit() {
        return downlimit;
    }

    public void setDownlimit(BigDecimal downlimit) {
        this.downlimit = downlimit;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }
}
