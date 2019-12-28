package org.jeecg.modules.basic.service.impl;

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
    public MaterialPrice getMtlPrice(String customerTypeId, String mtlId, String unitId) {

        MaterialPrice mtlPrice = new MaterialPrice();
        mtlPrice.setCustomerTypeId(customerTypeId);
        mtlPrice.setMaterialId(mtlId);
        mtlPrice.setUnitId(unitId);
        Map<String, String[]> params = new HashMap<>();
        params.put("materialId", new String[]{mtlId});
        params.put("unitId", new String[]{unitId});
        if (StringUtils.isNotBlank(customerTypeId)) {
            params.put("customerTypeId", new String[]{customerTypeId});
        }
        QueryWrapper<MaterialPrice> queryWrapper = QueryGenerator.initQueryWrapper(mtlPrice, params);
        return materialPriceMapper.selectOne(queryWrapper);
    }
}
