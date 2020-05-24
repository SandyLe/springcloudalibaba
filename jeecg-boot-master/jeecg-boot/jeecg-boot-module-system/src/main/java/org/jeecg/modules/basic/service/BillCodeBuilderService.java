package org.jeecg.modules.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.basic.entity.BillCodeBuilder;

public interface BillCodeBuilderService extends IService<BillCodeBuilder> {

    public String getBillCode(Integer billType);

    public String getBillCode(Integer billType, String companyId, String companyCode);

}
