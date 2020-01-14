package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.mapper.InventoryInMapper;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryInServiceImpl extends ServiceImpl<InventoryInMapper, InventoryIn> implements InventoryInService {

    @Autowired
    private InventoryInMapper inventoryInMapper;

}
