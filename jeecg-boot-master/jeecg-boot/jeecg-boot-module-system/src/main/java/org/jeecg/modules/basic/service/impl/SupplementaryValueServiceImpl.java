package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.SupplementaryValue;
import org.jeecg.modules.basic.mapper.SupplementaryValueMapper;
import org.jeecg.modules.basic.service.SupplementaryValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplementaryValueServiceImpl extends ServiceImpl<SupplementaryValueMapper, SupplementaryValue> implements SupplementaryValueService{

    @Autowired
    private SupplementaryValueMapper supplementaryValueMapper;

    @Override
    public SupplementaryValue findBySourceIdCode(String sourceId, String code) {

        LambdaQueryWrapper<SupplementaryValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SupplementaryValue::getSourceId, sourceId);
        lambdaQueryWrapper.eq(SupplementaryValue::getCode, code);
        return super.getOne(lambdaQueryWrapper);
    }
}
