package org.jeecg.modules.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;
import org.jeecg.modules.purchase.mapper.PurchaseReturnMtlMapper;
import org.jeecg.modules.purchase.service.PurchaseReturnMtlService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 */
@Service
public class PurchaseReturnMtlServiceImpl extends ServiceImpl<PurchaseReturnMtlMapper, PurchaseReturnMtl> implements PurchaseReturnMtlService {

    /**
     * 根据总表ID查询详情
     * @param sourceId
     */
    public List<PurchaseReturnMtl> queryBySourceId(@Param("sourceId")String sourceId){
        return baseMapper.queryBySourceId(sourceId);
    }
}
