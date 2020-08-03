package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.saleorder.entity.SaleOrderChannel;
import org.jeecg.modules.saleorder.entity.SaleOrderCost;
import org.jeecg.modules.saleorder.mapper.SaleOrderChannelMapper;
import org.jeecg.modules.saleorder.mapper.SaleOrderCostMapper;
import org.jeecg.modules.saleorder.service.SaleOrderChannelService;
import org.jeecg.modules.saleorder.service.SaleOrderCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleOrderChannelServiceImpl extends ServiceImpl<SaleOrderChannelMapper, SaleOrderChannel> implements SaleOrderChannelService {

}
