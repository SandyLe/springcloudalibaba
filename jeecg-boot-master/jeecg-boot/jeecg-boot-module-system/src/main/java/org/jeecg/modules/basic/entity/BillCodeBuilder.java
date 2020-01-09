package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.enums.DateFormat;
import org.jeecg.modules.basic.enums.EnumConvertUtils;

@TableName("sl_bill_code_builder")
@ApiModel(value = "BillCodeBuilder", description = "编号规则")
public class BillCodeBuilder extends BasicEntity {

    @ApiModelProperty("单据类型")
    private Integer billType;

    @ApiModelProperty("前缀缩写")
    private String prefix;

    @ApiModelProperty("编号位数")
    private Integer zeroCount;

    @ApiModelProperty("日期格式")
    private Integer dateFmtId;

    @ApiModelProperty("是否有日期")
    private Boolean hasDate;

    @ApiModelProperty("当前段位")
    private Long currentLevel;

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getZeroCount() {
        return zeroCount;
    }

    public void setZeroCount(Integer zeroCount) {
        this.zeroCount = zeroCount;
    }

    public Integer getDateFmtId() {
        return dateFmtId;
    }

    public void setDateFmtId(Integer dateFmtId) {
        this.dateFmtId = dateFmtId;
    }

    public Boolean getHasDate() {
        return hasDate;
    }

    public Long getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Long currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setHasDate(Boolean hasDate) {
        this.hasDate = hasDate;
    }

    //回显日期格式
    public String getDateFormatName() {

        return EnumConvertUtils.getName(DateFormat.class, dateFmtId);
    }

    //回显billtype名称
    public String getBillTypeName() {

        return EnumConvertUtils.getName(BillType.class, billType);
    }
}
