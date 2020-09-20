package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.MaterialAuxiliaryItem;
import org.jeecg.modules.basic.mapper.MaterialAuxiliaryItemMapper;
import org.jeecg.modules.basic.service.MaterialAuxiliaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialAuxiliaryItemServiceImpl extends ServiceImpl<MaterialAuxiliaryItemMapper, MaterialAuxiliaryItem> implements MaterialAuxiliaryItemService{

    @Autowired
    private MaterialAuxiliaryItemMapper materialAuxiliaryItemMapper;
}
