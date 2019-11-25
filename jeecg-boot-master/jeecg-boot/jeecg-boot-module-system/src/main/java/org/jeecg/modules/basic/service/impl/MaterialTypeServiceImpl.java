package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.MaterialType;
import org.jeecg.modules.basic.mapper.MaterialTypeMapper;
import org.jeecg.modules.basic.service.MaterialTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialTypeServiceImpl extends ServiceImpl<MaterialTypeMapper, MaterialType> implements MaterialTypeService {

    @Autowired
    private MaterialTypeMapper materialTypeMapper;
}
