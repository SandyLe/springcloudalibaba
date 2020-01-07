package org.jeecg.modules.purchase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.purchase.entity.Purchasedtl;

import java.util.List;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/4 15:03
 * @Version: V1.0
 */
public interface IPurchasedtlService extends IService<Purchasedtl> {
    /**
     * 根据总表ID删除详情
     * @param sourceId
     */
    void removeBySourceId(@Param("sourceId") String sourceId);
    /**
     * 根据总表ID删除详情
     * @param sourceIds
     */
    void removeBySourceIds(@Param("sourceIds") List<String> sourceIds);
}
