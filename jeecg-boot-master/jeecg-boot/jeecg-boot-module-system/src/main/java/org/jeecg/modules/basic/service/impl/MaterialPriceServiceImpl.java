package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.MaterialPrice;
import org.jeecg.modules.basic.mapper.MaterialPriceMapper;
import org.jeecg.modules.basic.service.MaterialPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MaterialPriceServiceImpl extends ServiceImpl<MaterialPriceMapper, MaterialPrice> implements MaterialPriceService {

    @Autowired
    private MaterialPriceMapper materialPriceMapper;

    @Override
    public MaterialPrice getMtlPrice(String ccId, String mtlId, String unitId) {

        LambdaQueryWrapper<MaterialPrice> lambdaQueryWrapper = new LambdaQueryWrapper<MaterialPrice>();
        lambdaQueryWrapper.eq(MaterialPrice::getCompanyId, ccId);
        lambdaQueryWrapper.eq(MaterialPrice::getMaterialId, mtlId);
        if (StringUtils.isNotBlank(unitId)) {
            lambdaQueryWrapper.eq(MaterialPrice::getUnitId, unitId);
        }
        return materialPriceMapper.selectOne(lambdaQueryWrapper);
    }

}
