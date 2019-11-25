package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.MaterialBrand;
import org.jeecg.modules.basic.mapper.MaterialBrandMapper;
import org.jeecg.modules.basic.service.MaterialBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialBrandServiceImpl extends ServiceImpl<MaterialBrandMapper, MaterialBrand> implements MaterialBrandService {

    @Autowired
    private MaterialBrandMapper materialBrandMapper;
}
