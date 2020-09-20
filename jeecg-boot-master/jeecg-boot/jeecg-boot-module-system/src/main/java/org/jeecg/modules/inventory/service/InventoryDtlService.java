package org.jeecg.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.inventory.entity.InventoryDtl;

import java.util.List;

public interface InventoryDtlService extends IService<InventoryDtl> {

    public InventoryDtl findInventoryDtl(String warehouseId, String batchNo, String mtlId, String unitId, String auxiliaryId);

    public List<InventoryDtl> findAvailableIntoryDtlList(String warehouseId, String mtlId, String unitId, String auxiliaryId);

}
