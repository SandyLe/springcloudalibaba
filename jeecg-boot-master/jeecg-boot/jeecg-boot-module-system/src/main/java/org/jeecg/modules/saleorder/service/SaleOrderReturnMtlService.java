package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnMtl;

import java.math.BigDecimal;

public interface SaleOrderReturnMtlService extends IService<SaleOrderReturnMtl> {
    public BigDecimal saveSaleOrderReturnMtl(SaleOrderReturnMtl saleOrderReturnMtl);
}
