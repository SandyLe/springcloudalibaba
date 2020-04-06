package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecg.modules.basic.enums.EnumConvertUtils;
import org.jeecg.modules.basic.enums.RowSts;

@ApiModel(value = "CBasicEntity", description = "企业基础数据基类")
public class CBasicEntity extends BasicEntity {

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
