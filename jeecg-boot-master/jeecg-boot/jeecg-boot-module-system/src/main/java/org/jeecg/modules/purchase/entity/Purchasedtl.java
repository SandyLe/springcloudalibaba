package org.jeecg.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.basic.entity.BasicEntity;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/4 14:33
 * @Version: V1.0
 */
@Data
@TableName("sl_purchasedtl")
public class Purchasedtl extends BasicEntity {

    /*采购订单id*/
    private String sourceId;

    /*产品id*/
    private String mtlId;

    /*单位*/
    private String unitId;

    /*数量*/
    private String quantity;

    /*单价*/
    private String price;

    /*折扣*/
    private String discount;

    /*金额*/
    private String amount;
}
