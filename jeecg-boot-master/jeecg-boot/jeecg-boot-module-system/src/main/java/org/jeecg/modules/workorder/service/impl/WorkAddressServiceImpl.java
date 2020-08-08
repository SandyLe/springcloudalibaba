package org.jeecg.modules.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.workorder.entity.WorkAddress;
import org.jeecg.modules.workorder.mapper.WorkAddressMapper;
import org.jeecg.modules.workorder.service.WorkAddressService;
import org.springframework.stereotype.Service;

@Service
public class WorkAddressServiceImpl extends ServiceImpl<WorkAddressMapper, WorkAddress> implements WorkAddressService {

    @Override
    public WorkAddress findBySouorceId(String sourceId) {

        LambdaQueryWrapper<WorkAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WorkAddress::getSourceId, sourceId);
        return getOne(lambdaQueryWrapper);
    }
}
