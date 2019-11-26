package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.mapper.WarehouseMapper;
import org.jeecg.modules.basic.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;
}
