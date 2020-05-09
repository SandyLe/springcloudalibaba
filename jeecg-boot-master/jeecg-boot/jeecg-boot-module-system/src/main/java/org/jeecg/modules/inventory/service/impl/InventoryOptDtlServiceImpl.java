package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.inventory.entity.InventoryOptDtl;
import org.jeecg.modules.inventory.mapper.InventoryOptDtlMapper;
import org.jeecg.modules.inventory.service.InventoryOptDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InventoryOptDtlServiceImpl extends ServiceImpl<InventoryOptDtlMapper, InventoryOptDtl>  implements InventoryOptDtlService {

    @Autowired
    private InventoryOptDtlMapper inventoryOptDtlMapper;

}
