package org.jeecg.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;
import org.jeecg.modules.inventory.entity.InventoryIn;

import java.util.List;

public interface InventoryInService extends IService<InventoryIn> {

    /**
     * 待入库产品列表
     *
     * @param id
     * @param sourceId
     * @return
     */
    public List<PreInventoryOutMtl> getDeliveryMtlList(String id, String sourceId);

    /**
     * 保存入库
     * @param inventoryIn
     * @return
     */
    public String saveToInventoryIn (InventoryIn inventoryIn);

    /**
     * 入库
     *
     * @param mtls
     * @return
     */
    public Boolean stockIn(List<PreInventoryOutMtl> mtls);

    /**
     * 查询原单出库单
     * @param sourceBillType
     * @param sourceId
     * @return
     */
    public InventoryIn queryBySourceId(Integer sourceBillType, String sourceId);

    /**
     * 根据原单ID删除数据
     * @param sourceId
     */
    public void deleteBySourceId(Integer sourceBillType, String sourceId);
}
