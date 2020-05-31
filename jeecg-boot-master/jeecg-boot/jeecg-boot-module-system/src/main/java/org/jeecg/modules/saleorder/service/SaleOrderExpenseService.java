package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.entity.SaleOrderExpense;

import java.math.BigDecimal;

public interface SaleOrderExpenseService extends IService<SaleOrderExpense> {

    public BigDecimal saveSaleOrderExpense(SaleOrderExpense saleOrderExpense);

    public BigDecimal updateSaleOrderTotalAmount(String billId);
}
