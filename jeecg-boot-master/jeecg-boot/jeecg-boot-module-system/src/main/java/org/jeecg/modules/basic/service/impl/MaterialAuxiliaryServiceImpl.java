package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.MaterialAuxiliary;
import org.jeecg.modules.basic.mapper.MaterialAuxiliaryMapper;
import org.jeecg.modules.basic.service.MaterialAuxiliaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialAuxiliaryServiceImpl extends ServiceImpl<MaterialAuxiliaryMapper, MaterialAuxiliary> implements MaterialAuxiliaryService{

    @Autowired
    private MaterialAuxiliaryMapper materialAuxiliaryMapper;
}
