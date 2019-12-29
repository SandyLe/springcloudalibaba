package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.basic.enums.DiscountType;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderExpense;
import org.jeecg.modules.saleorder.mapper.SaleOrderExpenseMapper;
import org.jeecg.modules.saleorder.service.SaleOrderExpenseService;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleOrderExpenseServiceImpl extends ServiceImpl<SaleOrderExpenseMapper, SaleOrderExpense>  implements SaleOrderExpenseService {

    @Autowired
    private SaleOrderExpenseMapper saleOrderExpenseMapper;

    @Autowired
    private SaleOrderService saleOrderService;

    @Override
    @Transactional
    public BigDecimal saveSaleOrderExpense(SaleOrderExpense saleOrderExpense){

        if (StringUtils.isNotBlank(saleOrderExpense.getId())) {
            super.updateById(saleOrderExpense);
        } else {
            super.save(saleOrderExpense);
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<String,Object> columns = new HashMap<>();
        columns.put("source_id", saleOrderExpense.getSourceId());
        Collection<SaleOrderExpense> list = listByMap(columns);
        for (SaleOrderExpense o: list){
            if (null != o.getAmount()){
                totalAmount = totalAmount.add(o.getAmount());
            }
        }
        SaleOrder saleOrder = saleOrderService.getById(saleOrderExpense.getSourceId());
        saleOrder.setOtheramount(totalAmount);
        BigDecimal tempTotalAmount = totalAmount;
        if (null != saleOrder.getMtlamount()){
            tempTotalAmount = tempTotalAmount.add(saleOrder.getMtlamount());
        }
        saleOrder.setTotalamount(tempTotalAmount);
        saleOrderService.updateById(saleOrder);
        return tempTotalAmount;
    }
}
