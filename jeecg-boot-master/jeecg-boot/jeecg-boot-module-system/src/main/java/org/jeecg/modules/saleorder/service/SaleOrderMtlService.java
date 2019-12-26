package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;

import java.math.BigDecimal;

public interface SaleOrderMtlService extends IService<SaleOrderMtl> {
    public BigDecimal saveSaleOrderMtl(SaleOrderMtl saleOrderMtl);
}
