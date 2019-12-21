package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.MaterialPrice;
import org.jeecg.modules.basic.mapper.MaterialPriceMapper;
import org.jeecg.modules.basic.service.MaterialPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialPriceServiceImpl extends ServiceImpl<MaterialPriceMapper, MaterialPrice> implements MaterialPriceService {

    @Autowired
    private MaterialPriceMapper materialPriceMapper;
}
