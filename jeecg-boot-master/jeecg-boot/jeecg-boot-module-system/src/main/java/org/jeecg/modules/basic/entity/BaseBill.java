package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.common.enums.RowSts;
import org.jeecg.common.system.base.entity.JeecgEntity;

@ApiModel(value = "BaseBill", description = "基础单据")
public class BaseBill extends JeecgEntity {

    @ApiModelProperty("企业ID")
    private String companyId;
    @TableField(exist=false)
    private String companyName;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
