package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.jeecg.common.constant.Constant;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.logistics.dto.LogisticsOrderDto;
import org.jeecg.modules.logistics.entity.LogisticsOrder;
import org.jeecg.modules.logistics.entity.LogisticsOrderDtl;
import org.jeecg.modules.logistics.service.LogisticsOrderService;
import org.jeecg.modules.saleorder.entity.SaleOrderDeliveryInfo;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.mapper.SaleOrderDeliveryInfoMapper;
import org.jeecg.modules.saleorder.service.SaleOrderDeliveryInfoService;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SaleOrderDeliveryInfoServiceImpl extends ServiceImpl<SaleOrderDeliveryInfoMapper, SaleOrderDeliveryInfo> implements SaleOrderDeliveryInfoService{

    @Autowired
    private SaleOrderDeliveryInfoMapper saleOrderDeliveryInfoMapper;

    @Autowired
    private LogisticsOrderService logisticsOrderService;

    @Lazy
    @Autowired
    private SaleOrderMtlService saleOrderMtlService;

    @Autowired
    private BillCodeBuilderService billCodeBuilderService;


    @Override
    public String saveSaleOrderDelivery(SaleOrderDeliveryInfo saleOrderDeliveryInfo) {

        super.save(saleOrderDeliveryInfo);
        if (Constant.DELIVERY_DIANZIMIANDAN.equals(saleOrderDeliveryInfo.getCdiDefaultType()) ||
            Constant.DELIVERY_WULIU.equals(saleOrderDeliveryInfo.getCdiDefaultType())) {
            LogisticsOrderDto orderDto = new LogisticsOrderDto();
            BeanUtils.copyProperties(saleOrderDeliveryInfo, orderDto);
            orderDto.setBillDate(new Date());
            orderDto.setBillStatus(BillStatus.NEW.getId());
            orderDto.setBillType(BillType.LOGISTICSORDER.getId());
            orderDto.setSourceBillType(BillType.SALEORDER.getId());
            orderDto.setSourceCode(saleOrderDeliveryInfo.getSourceBillCode());

            List<LogisticsOrderDtl> dtls = Lists.newArrayList();
            BigDecimal totalQty = BigDecimal.ZERO;
            LambdaQueryWrapper<SaleOrderMtl> lambdaQueryWrapper = new LambdaQueryWrapper<SaleOrderMtl>().eq(SaleOrderMtl::getSourceId, saleOrderDeliveryInfo.getSourceId());
            List<SaleOrderMtl> saleOrderMtls = saleOrderMtlService.list(lambdaQueryWrapper);
            for(SaleOrderMtl o : saleOrderMtls){
                LogisticsOrderDtl dtl = new LogisticsOrderDtl();
                BeanUtils.copyProperties(o, dtl, "sourceId");
                dtl.setSourceId(orderDto.getId());
                dtls.add(dtl);
                totalQty = totalQty.add(o.getQuantity());
            };
            orderDto.setDetaillist(dtls);
            orderDto.setNumber(totalQty);
            logisticsOrderService.saveOrder(orderDto);
        }
        return saleOrderDeliveryInfo.getId();
    }
}
