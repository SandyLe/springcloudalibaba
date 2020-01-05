package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.inventory.entity.Inventory;
import org.jeecg.modules.inventory.mapper.InventoryMapper;
import org.jeecg.modules.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory>  implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public Inventory findInventory(String warehouseId, String mtlId, String unitId) {


        Inventory inventory = new Inventory();
        inventory.setWarehouseId(warehouseId);
        inventory.setMtlId(mtlId);
        inventory.setUnitId(unitId);
        Map<String, String[]> params = new HashMap<>();
        params.put("mtlId", new String[]{mtlId});
        params.put("warehouseId", new String[]{warehouseId});
        if (StringUtils.isNotBlank(unitId)) {
            params.put("unitId", new String[]{unitId});
        }
        QueryWrapper<Inventory> queryWrapper = QueryGenerator.initQueryWrapper(inventory, params);
        return inventoryMapper.selectOne(queryWrapper);
    }
}
