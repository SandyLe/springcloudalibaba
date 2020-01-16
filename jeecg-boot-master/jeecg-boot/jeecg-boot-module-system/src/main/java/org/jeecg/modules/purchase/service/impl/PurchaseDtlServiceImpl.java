package org.jeecg.modules.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.purchase.entity.PurchaseDtl;
import org.jeecg.modules.purchase.mapper.IPurchaseDtlMapper;
import org.jeecg.modules.purchase.service.IPurchaseDtlService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/4 15:44
 * @Version: V1.0
 */
@Service
public class PurchaseDtlServiceImpl extends ServiceImpl<IPurchaseDtlMapper, PurchaseDtl> implements IPurchaseDtlService {
    /**
     * 根据总表ID删除详情
     * @param sourceId
     */
    public void removeBySourceId(@Param("sourceId") String sourceId){
        baseMapper.removeBySourceId(sourceId);
    }
    /**
     * 根据总表ID删除详情
     * @param sourceIds
     */
    public void removeBySourceIds(@Param("sourceIds") List<String> sourceIds){
        baseMapper.removeBySourceIds(sourceIds);
    }
    /**
     * 根据总表ID查询详情
     * @param sourceId
     */
    public List<PurchaseDtl> queryBySourceId(@Param("sourceId")String sourceId){
        return baseMapper.queryBySourceId(sourceId);
    }
}
