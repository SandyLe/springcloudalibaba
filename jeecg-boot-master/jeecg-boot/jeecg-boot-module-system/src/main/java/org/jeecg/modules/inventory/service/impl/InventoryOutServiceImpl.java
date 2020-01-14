package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.mapper.InventoryOutMapper;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryOutServiceImpl extends ServiceImpl<InventoryOutMapper, InventoryOut>  implements InventoryOutService {

    @Autowired
    private InventoryOutMapper inventoryOutMapper;

}
