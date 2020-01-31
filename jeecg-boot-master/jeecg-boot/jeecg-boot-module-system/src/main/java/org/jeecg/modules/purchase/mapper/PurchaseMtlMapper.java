package org.jeecg.modules.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.purchase.entity.PurchaseMtl;

import java.util.List;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/4 14:57
 * @Version: V1.0
 */
public interface PurchaseMtlMapper extends BaseMapper<PurchaseMtl> {
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

    /**
     * 根据总表ID查询详情
     * @param sourceId
     */
    List<PurchaseMtl> queryBySourceId(@Param("sourceId")String sourceId);
}
