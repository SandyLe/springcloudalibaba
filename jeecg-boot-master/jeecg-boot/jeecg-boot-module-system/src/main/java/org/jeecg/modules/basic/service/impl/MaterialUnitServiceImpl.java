package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.mapper.MaterialUnitMapper;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialUnitServiceImpl extends ServiceImpl<MaterialUnitMapper, MaterialUnit> implements MaterialUnitService {

    @Autowired
    private MaterialUnitMapper materialUnitMapper;
}
