package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnExpense;

import java.math.BigDecimal;

public interface SaleOrderReturnExpenseService extends IService<SaleOrderReturnExpense> {
    public BigDecimal saveSaleOrderReturnExpense(SaleOrderReturnExpense saleOrderExpense);
}
