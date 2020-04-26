package org.jeecg.modules.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.logistics.dto.LogisticsOrderDto;
import org.jeecg.modules.logistics.entity.LogisticsOrder;
import org.jeecg.modules.logistics.entity.LogisticsOrderDtl;
import org.jeecg.modules.logistics.mapper.LogisticsOrderMapper;
import org.jeecg.modules.logistics.service.LogisticsOrderDtlService;
import org.jeecg.modules.logistics.service.LogisticsOrderService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
@Service
public class LogisticsOrderServiceImpl extends ServiceImpl<LogisticsOrderMapper, LogisticsOrder> implements LogisticsOrderService {

    @Autowired
    private LogisticsOrderMapper changeOrderMapper;
    @Autowired
    private LogisticsOrderDtlService changeOrderDtlService;
    @Lazy
    @Autowired
    private InventoryOutService inventoryOutService;
    @Lazy
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    @Transactional
    public String saveOrder(LogisticsOrderDto logisticsOrderDto){
        // 物流主表
        String code = billCodeBuilderService.getBillCode(BillType.LOGISTICSORDER.getId());
        logisticsOrderDto.setCode(code);
        logisticsOrderDto.setBillStatus(BillStatus.NEW.getId());
        super.save(logisticsOrderDto);

        //物流单子表
        if (CollectionUtils.isNotEmpty(logisticsOrderDto.getDetaillist())){
            List<LogisticsOrderDtl> mtls = logisticsOrderDto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //组装商品详情
                o.setCode(code);
                o.setSourceId(logisticsOrderDto.getId());
            });
            changeOrderDtlService.saveBatch(mtls);
        }

        return logisticsOrderDto.getId();
    }

    @Override
    @Transactional
    public String editOrder(LogisticsOrderDto logisticsOrderDto){

        // 组装主表
        super.updateById(logisticsOrderDto);
        if (logisticsOrderDto.getDetaillist().size() > 0){
            for (LogisticsOrderDtl item: logisticsOrderDto.getDetaillist()){
                //组装商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    changeOrderDtlService.updateById(item);
                else{
                    item.setSourceId(logisticsOrderDto.getId());
                    changeOrderDtlService.save(item);
                }
            }
        }
        return logisticsOrderDto.getId();
    }
}
