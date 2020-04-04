package org.jeecg.modules.basic.dto;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class MaterialPriceExcelDto {

    @Excel(name = "产品名称", width = 15)
    private String name;

    @Excel(name = "客户类型", width = 15)
    private String customerType;

    @Excel(name = "计量单位", width = 15)
    private String unit;

    @Excel(name = "价格", width = 15)
    private String price;

}
