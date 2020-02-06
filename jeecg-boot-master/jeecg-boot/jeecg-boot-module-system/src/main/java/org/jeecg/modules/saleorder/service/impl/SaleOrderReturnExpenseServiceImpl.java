package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderReturn;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnExpense;
import org.jeecg.modules.saleorder.mapper.SaleOrderReturnExpenseMapper;
import org.jeecg.modules.saleorder.service.SaleOrderReturnExpenseService;
import org.jeecg.modules.saleorder.service.SaleOrderReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleOrderReturnExpenseServiceImpl extends ServiceImpl<SaleOrderReturnExpenseMapper, SaleOrderReturnExpense>  implements SaleOrderReturnExpenseService {

    @Autowired
    private SaleOrderReturnExpenseMapper saleOrderReturnExpenseMapper;

    @Autowired
    private SaleOrderReturnService saleOrderReturnService;

    @Override
    @Transactional
    public BigDecimal saveSaleOrderReturnExpense(SaleOrderReturnExpense saleOrderReturnExpense){

        if (StringUtils.isNotBlank(saleOrderReturnExpense.getId())) {
            super.updateById(saleOrderReturnExpense);
        } else {
            super.save(saleOrderReturnExpense);
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<String,Object> columns = new HashMap<>();
        columns.put("source_id", saleOrderReturnExpense.getSourceId());
        Collection<SaleOrderReturnExpense> list = listByMap(columns);
        for (SaleOrderReturnExpense o: list){
            if (null != o.getAmount()){
                totalAmount = totalAmount.add(o.getAmount());
            }
        }
        SaleOrderReturn saleOrderReturn = saleOrderReturnService.getById(saleOrderReturnExpense.getSourceId());
        saleOrderReturn.setOtheramount(totalAmount);
        BigDecimal tempTotalAmount = BigDecimal.ZERO.add(totalAmount);
        if (null != saleOrderReturn.getMtlamount()){
            tempTotalAmount = tempTotalAmount.add(saleOrderReturn.getMtlamount());
        }
        saleOrderReturn.setTotalamount(tempTotalAmount);
        saleOrderReturnService.updateById(saleOrderReturn);
        return tempTotalAmount;
    }
}
