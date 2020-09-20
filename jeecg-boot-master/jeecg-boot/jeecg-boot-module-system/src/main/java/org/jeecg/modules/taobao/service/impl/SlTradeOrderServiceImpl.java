package org.jeecg.modules.taobao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.taobao.entity.SlTradeOrder;
import org.jeecg.modules.taobao.mapper.SlTradeOrderMapper;
import org.jeecg.modules.taobao.service.SlTradeOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlTradeOrderServiceImpl extends ServiceImpl<SlTradeOrderMapper, SlTradeOrder> implements SlTradeOrderService {

    @Override
    public boolean saveSlTradeOrder(SlTradeOrder entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean saveSlTradeOrderBatch(List<SlTradeOrder> slTradeOrderList) {
        return super.saveOrUpdateBatch(slTradeOrderList);
    }

    @Override
    public List<SlTradeOrder> findByTid(Long tid) {
        return list(new LambdaQueryWrapper<SlTradeOrder>().eq(SlTradeOrder::getOid, tid));
    }
}
