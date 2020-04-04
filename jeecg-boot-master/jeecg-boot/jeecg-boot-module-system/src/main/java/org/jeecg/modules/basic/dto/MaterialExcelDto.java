package org.jeecg.modules.basic.dto;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;


@Data
public class MaterialExcelDto {

    @Excel(name = "名称", width = 15)
    private String name;


    @Excel(name = "编码", width = 15)
    private String code;


    @Excel(name = "规格", width = 15)
    private String specification;


    @Excel(name = "品牌", width = 15)
    private String brand;


    @Excel(name = "类型", width = 15)
    private String type;


    @Excel(name = "单位", width = 15)
    private String unit;


}
