package org.jeecg.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("sl_purchasereturn")
public class Purchasereturn extends BasicEntity {

    /*备注*/
    @Excel(name = "备注", width = 15)
    private String content;

    /*仓库id*/
    @Excel(name = "仓库id", width = 15)
    private String warehouseId;

    /*金额*/
    @Excel(name = "金额",width = 20)
    private String amount;

    /*订单日期*/
    @Excel(name = "订单日期",width = 20 , format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date billdate;

}
