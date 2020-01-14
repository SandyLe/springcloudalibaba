package org.jeecg.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 采购列表
 * @Author: tomkluas
 * @Date: 2019/12/27 15:24
 * @Version: V1.0
 */
@Data
@TableName("sl_purchase")
public class Purchase extends BasicEntity {

    /*供应商*/
    @Excel(name = "供应商", width = 15)
    private String vendorId;

    /*仓库id*/
    @Excel(name = "仓库id", width = 15)
    private String warehouseId;

    /*结算账户*/
    @Excel(name = "结算账户",width = 20)
    private String account;

    /*实付金额*/
    @Excel(name = "实付金额",width = 20)
    private String payamount;

    /*订单日期*/
    @Excel(name = "订单日期",width = 20 , format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date billdate;

    /*总金额*/
    @Excel(name = "总金额",width = 20)
    private String totalamount;


}
