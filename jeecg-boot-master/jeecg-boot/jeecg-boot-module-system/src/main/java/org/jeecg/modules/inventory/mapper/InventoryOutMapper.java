package org.jeecg.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.inventory.entity.InventoryOut;

import java.util.List;

public interface InventoryOutMapper extends BaseMapper<InventoryOut> {
    /**
     * 根据总表ID查询详情
     * @param sourceId
     */
    InventoryOut queryBySourceId(@Param("sourceId")String sourceId);
}
