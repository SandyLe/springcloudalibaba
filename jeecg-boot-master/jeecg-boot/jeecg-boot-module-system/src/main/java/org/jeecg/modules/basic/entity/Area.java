package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@TableName("sl_area")
@ApiModel(value = "Area", description = "地区")
public class Area extends BasicEntity {

    @ApiModelProperty("父级ID")
    private String parentId;
    @ApiModelProperty("简称")
    private String shortName;
    @ApiModelProperty("等级")
    private Integer levelType;
    @ApiModelProperty("城市代码")
    private String cityCode;
    @ApiModelProperty("区域代码")
    private String zipCode;
    @ApiModelProperty("组装")
    private String mergeName;
    @ApiModelProperty("经度")
    private BigDecimal lng;
    @ApiModelProperty("纬度")
    private BigDecimal lat;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMergeName() {
        return mergeName;
    }

    public void setMergeName(String mergeName) {
        this.mergeName = mergeName;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }
}
