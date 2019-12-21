package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.MaterialSelfUnit;
import org.jeecg.modules.basic.mapper.MaterialSelfUnitMapper;
import org.jeecg.modules.basic.service.MaterialSelfUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialSelfUnitServiceImpl extends ServiceImpl<MaterialSelfUnitMapper, MaterialSelfUnit> implements MaterialSelfUnitService {

    @Autowired
    private MaterialSelfUnitMapper materialSelfUnitMapper;
}
