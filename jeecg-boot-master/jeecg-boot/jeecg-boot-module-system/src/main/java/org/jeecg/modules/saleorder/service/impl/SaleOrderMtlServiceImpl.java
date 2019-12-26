package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.basic.enums.DiscountType;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.mapper.SaleOrderMtlMapper;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleOrderMtlServiceImpl extends ServiceImpl<SaleOrderMtlMapper, SaleOrderMtl>  implements SaleOrderMtlService {

    @Autowired
    private SaleOrderMtlMapper saleOrderMtlMapper;

    @Autowired
    private SaleOrderService saleOrderService;

    @Override
    @Transactional
    public BigDecimal saveSaleOrderMtl(SaleOrderMtl saleOrderMtl){

        if (StringUtils.isNotBlank(saleOrderMtl.getDiscountType())) {
            if (DiscountType.PERCENT.getId().equals(saleOrderMtl.getDiscountType())){
                saleOrderMtl.setAmount(saleOrderMtl.getQuantity().multiply(saleOrderMtl.getPrice()).multiply(saleOrderMtl.getDiscount().divide(new BigDecimal(100), 8, RoundingMode.CEILING)));
            } else if (DiscountType.AMOUNT.getId().equals(saleOrderMtl.getDiscountType())) {
                saleOrderMtl.setAmount(saleOrderMtl.getQuantity().multiply(saleOrderMtl.getPrice()).subtract(saleOrderMtl.getDiscount()));
            }
        }
        super.save(saleOrderMtl);
        BigDecimal totalAmount = BigDecimal.ONE;
        Map<String,Object> columns = new HashMap<>();
        columns.put("sourceId", saleOrderMtl.getSourceId());
        Collection<SaleOrderMtl> list = listByMap(columns);
        for (SaleOrderMtl o: list){
            totalAmount = totalAmount.add(o.getAmount());
        }
        SaleOrder saleOrder = saleOrderService.getById(saleOrderMtl.getSourceId());
        saleOrder.setTotalamount(totalAmount);
        saleOrderService.updateById(saleOrder);
        return totalAmount;
    }
}
