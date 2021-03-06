package org.jeecg.modules.stocking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.jmx.snmp.EnumRowStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.InventoryOperation;
import org.jeecg.modules.inventory.entity.InventoryLog;
import org.jeecg.modules.inventory.service.InventoryService;
import org.jeecg.modules.stocking.entity.Stocking;
import org.jeecg.modules.stocking.mapper.StockingMapper;
import org.jeecg.modules.stocking.service.StockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class StockingServiceImpl extends ServiceImpl<StockingMapper, Stocking>  implements StockingService {

    @Autowired
    private StockingMapper stockingMapper;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    @Override
    public String handleStocking(String id) {
        Stocking stocking = getById(id);
        InventoryLog inventoryLog = new InventoryLog(id, id, BillType.STOCKING.getId(), stocking.getMtlId(), stocking.getWarehouseId(), stocking.getStockAmount(), BigDecimal.ZERO, stocking.getBeforeAmount(), stocking.getUnitId(),
                InventoryOperation.STOCKING.getId(),null, stocking.getCompanyId(), stocking.getAuxiliaryId());
        inventoryLog.setOperateTime(new Date());
        inventoryService.updateInventory(inventoryLog);
        stocking.setRowSts(EnumRowStatus.destroy);
        updateById(stocking);
        return inventoryLog.getId();
    }
}
