package org.jeecg.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.inventory.dto.PreInventoryDto;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;

import java.util.List;

public interface InventoryOutService extends IService<InventoryOut> {

    /**
     * 待出库产品列表
     *
     * @param id
     * @param sourceId
     * @return
     */
    public List<PreInventoryOutMtl> getDeliveryMtlList(String id, String sourceId);
    public String saveToInventoryOut (InventoryOut inventoryOut);
    /**
     * 销售出库
     *
     * @param preInventoryDto
     * @return
     */
    public Boolean stockOut(PreInventoryDto preInventoryDto);

    /**
     * 查询原单出库单
     * @param sourceBillType
     * @param sourceId
     * @return
     */
    public InventoryOut queryBySourceId(Integer sourceBillType, String sourceId);

    /**
     * 根据原单ID删除数据
     * @param sourceId
     */
    public void deleteBySourceId(Integer sourceBillType, String sourceId);
}
