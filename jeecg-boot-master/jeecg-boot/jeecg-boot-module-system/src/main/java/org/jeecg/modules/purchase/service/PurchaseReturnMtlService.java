package org.jeecg.modules.purchase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;

import java.util.List;

/**
 *
 */
public interface PurchaseReturnMtlService extends IService<PurchaseReturnMtl> {
    /**
     * 根据总表ID查询详情
     * @param sourceId
     */
    List<PurchaseReturnMtl> queryBySourceId(@Param("sourceId")String sourceId);
}
