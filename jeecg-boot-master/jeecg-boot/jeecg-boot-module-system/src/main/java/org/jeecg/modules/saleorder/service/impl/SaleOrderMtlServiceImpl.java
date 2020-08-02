package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.DiscountType;
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
    private SaleOrderService saleOrderService;

    @Override
    @Transactional
    public BigDecimal saveSaleOrderMtl(SaleOrderMtl saleOrderMtl){

        saleOrderMtl.setAmount(saleOrderMtl.getQuantity().multiply(saleOrderMtl.getPrice()));
        if (StringUtils.isNotBlank(saleOrderMtl.getDiscountType())) {
            if (DiscountType.PERCENT.getId().equals(saleOrderMtl.getDiscountType())){
                saleOrderMtl.setAmount(saleOrderMtl.getAmount().multiply(saleOrderMtl.getDiscount().divide(new BigDecimal(100), 8, RoundingMode.CEILING)));
            } else if (DiscountType.AMOUNT.getId().equals(saleOrderMtl.getDiscountType())) {
                saleOrderMtl.setAmount(saleOrderMtl.getAmount().subtract(saleOrderMtl.getDiscount()));
            }
        }
        if (StringUtils.isNotBlank(saleOrderMtl.getId())) {
            super.updateById(saleOrderMtl);
        } else {
            super.save(saleOrderMtl);
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<String,Object> columns = new HashMap<>();
        columns.put("source_id", saleOrderMtl.getSourceId());
        Collection<SaleOrderMtl> list = listByMap(columns);
        for (SaleOrderMtl o: list){
            if (null != o.getAmount()){
                totalAmount = totalAmount.add(o.getAmount());
            }
        }
        SaleOrder saleOrder = saleOrderService.getById(saleOrderMtl.getSourceId());
        saleOrder.setMtlamount(totalAmount);
        BigDecimal tempTotalAmount = BigDecimal.ZERO.add(totalAmount);
        if (null != saleOrder.getOtheramount()){
            tempTotalAmount = totalAmount.add(saleOrder.getOtheramount());
        }
        saleOrder.setTotalamount(tempTotalAmount);
        Boolean result = saleOrderService.saveOrUpdate(saleOrder);
        System.out.println(saleOrder.getId() + "," + result + "," + saleOrder.getTotalamount());
        return tempTotalAmount;
    }

    @Override
    public List<SaleOrderMtl> findList(String sourceId) {

        LambdaQueryWrapper<SaleOrderMtl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SaleOrderMtl::getSourceId, sourceId);
        return super.list(queryWrapper);
    }
}
