package org.jeecg.modules.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;

import java.util.List;

public interface PurchaseReturnMtlMapper extends BaseMapper<PurchaseReturnMtl> {
    /**
     * 根据总表ID查询详情
     * @param sourceId
     */
    List<PurchaseReturnMtl> queryBySourceId(@Param("sourceId")String sourceId);
}
