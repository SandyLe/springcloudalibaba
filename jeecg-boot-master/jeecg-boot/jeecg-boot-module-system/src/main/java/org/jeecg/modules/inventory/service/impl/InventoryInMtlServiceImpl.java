package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
import org.jeecg.modules.inventory.mapper.InventoryInMtlMapper;
import org.jeecg.modules.inventory.service.InventoryInMtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryInMtlServiceImpl extends ServiceImpl<InventoryInMtlMapper, InventoryInMtl> implements InventoryInMtlService {

    @Autowired
    private InventoryInMtlMapper inventoryInMtlMapper;

}
