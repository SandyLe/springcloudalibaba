package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.inventory.entity.InventoryOutMtl;
import org.jeecg.modules.inventory.mapper.InventoryOutMtlMapper;
import org.jeecg.modules.inventory.service.InventoryOutMtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryOutMtlServiceImpl extends ServiceImpl<InventoryOutMtlMapper, InventoryOutMtl>  implements InventoryOutMtlService {

    @Autowired
    private InventoryOutMtlMapper inventoryOutMtlMapper;

}
