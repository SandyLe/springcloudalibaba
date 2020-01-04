package org.jeecg.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class Purchasedtl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /*采购订单id*/
    private String sourceId;

    /*备注*/
    private String description;

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

    /*创建时间*/
    private Date createTime;
}
