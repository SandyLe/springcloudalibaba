package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.DiscountType;
import org.jeecg.modules.saleorder.entity.SaleOrderReturn;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnMtl;
import org.jeecg.modules.saleorder.mapper.SaleOrderReturnMtlMapper;
import org.jeecg.modules.saleorder.service.SaleOrderReturnMtlService;
import org.jeecg.modules.saleorder.service.SaleOrderReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleOrderReturnMtlServiceImpl extends ServiceImpl<SaleOrderReturnMtlMapper, SaleOrderReturnMtl>  implements SaleOrderReturnMtlService {

    @Autowired
    private SaleOrderReturnMtlMapper saleOrderReturnMtlMapper;

    @Autowired
    private SaleOrderReturnService saleOrderReturnService;

    @Override
    @Transactional
    public BigDecimal saveSaleOrderReturnMtl(SaleOrderReturnMtl saleOrderReturnMtl){

        saleOrderReturnMtl.setAmount(saleOrderReturnMtl.getQuantity().multiply(saleOrderReturnMtl.getPrice()));
        if (StringUtils.isNotBlank(saleOrderReturnMtl.getDiscountType())) {
            if (DiscountType.PERCENT.getId().equals(saleOrderReturnMtl.getDiscountType())){
                saleOrderReturnMtl.setAmount(saleOrderReturnMtl.getAmount().multiply(BigDecimal.ONE.subtract(saleOrderReturnMtl.getDiscount().divide(new BigDecimal(100), 8, RoundingMode.CEILING))));
            } else if (DiscountType.AMOUNT.getId().equals(saleOrderReturnMtl.getDiscountType())) {
                saleOrderReturnMtl.setAmount(saleOrderReturnMtl.getAmount().subtract(saleOrderReturnMtl.getDiscount()));
            }
        }
        if (StringUtils.isNotBlank(saleOrderReturnMtl.getId())) {
            super.updateById(saleOrderReturnMtl);
        } else {
            super.save(saleOrderReturnMtl);
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<String,Object> columns = new HashMap<>();
        columns.put("source_id", saleOrderReturnMtl.getSourceId());
        Collection<SaleOrderReturnMtl> list = listByMap(columns);
        for (SaleOrderReturnMtl o: list){
            if (null != o.getAmount()){
                totalAmount = totalAmount.add(o.getAmount());
            }
        }
        SaleOrderReturn saleOrderReturn = saleOrderReturnService.getById(saleOrderReturnMtl.getSourceId());
        saleOrderReturn.setMtlamount(totalAmount);
        BigDecimal tempTotalAmount = BigDecimal.ZERO.add(totalAmount);
        if (null != saleOrderReturn.getOtheramount()){
            tempTotalAmount = totalAmount.add(saleOrderReturn.getOtheramount());
        }
        saleOrderReturn.setTotalamount(tempTotalAmount);
        Boolean result = saleOrderReturnService.saveOrUpdate(saleOrderReturn);
        System.out.println(saleOrderReturn.getId() + "," + result + "," + saleOrderReturn.getTotalamount());
        return tempTotalAmount;
    }
}
