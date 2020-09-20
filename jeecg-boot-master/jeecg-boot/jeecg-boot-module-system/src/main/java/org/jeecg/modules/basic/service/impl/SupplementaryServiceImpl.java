package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.Supplementary;
import org.jeecg.modules.basic.mapper.SupplementaryMapper;
import org.jeecg.modules.basic.service.SupplementaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplementaryServiceImpl extends ServiceImpl<SupplementaryMapper, Supplementary> implements SupplementaryService{

    @Autowired
    private SupplementaryMapper supplementaryMapper;

    @Override
    public Supplementary findByCcIdCode(String companyId, String code) {

        LambdaQueryWrapper<Supplementary> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Supplementary::getCompanyId, companyId);
        lambdaQueryWrapper.eq(Supplementary::getCode, code);
        return super.getOne(lambdaQueryWrapper);
    }
}
