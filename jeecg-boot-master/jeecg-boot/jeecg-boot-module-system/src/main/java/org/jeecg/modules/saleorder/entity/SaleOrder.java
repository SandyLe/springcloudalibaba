package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.system.base.entity.JeecgEntity;

@Data
@TableName("sl_sale_order")
public class SaleOrder extends JeecgEntity {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
