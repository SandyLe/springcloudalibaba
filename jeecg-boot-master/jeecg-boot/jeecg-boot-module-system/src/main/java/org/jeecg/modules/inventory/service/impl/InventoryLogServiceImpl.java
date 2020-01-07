package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.inventory.entity.InventoryLog;
import org.jeecg.modules.inventory.mapper.InventoryLogMapper;
import org.jeecg.modules.inventory.service.InventoryLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryLogServiceImpl extends ServiceImpl<InventoryLogMapper, InventoryLog>  implements InventoryLogService {

    @Autowired
    private InventoryLogMapper inventoryLogMapper;
}
