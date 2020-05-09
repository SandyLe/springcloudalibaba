package org.jeecg.modules.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.PurchaseMtl;
import org.jeecg.modules.purchase.mapper.PurchaseMtlMapper;
import org.jeecg.modules.purchase.service.PurchaseMtlService;
import org.jeecg.modules.purchase.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/4 15:44
 * @Version: V1.0
 */
@Service
public class PurchaseMtlServiceImpl extends ServiceImpl<PurchaseMtlMapper, PurchaseMtl> implements PurchaseMtlService {

    @Autowired
    private PurchaseService purchaseService;
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
    public List<PurchaseMtl> queryBySourceId(@Param("sourceId")String sourceId){
        return baseMapper.queryBySourceId(sourceId);
    }

    @Override
    public List<PurchaseMtl> findPurchaseMtls(String batchNo, String mtlId) {

        LambdaQueryWrapper<PurchaseMtl> lambdaQueryWrapper = new LambdaQueryWrapper<PurchaseMtl>();
        if (StringUtils.isNotBlank(batchNo)) {
            List<Purchase> purchases = purchaseService.findByBatchNo(batchNo);
            if (CollectionUtils.isNotEmpty(purchases)) {
                List<String> ids = purchases.stream().map(o->o.getId()).collect(Collectors.toList());
                lambdaQueryWrapper.in(PurchaseMtl::getSourceId, ids);
            } else {
                return Lists.newArrayList();
            }
        }
        if (StringUtils.isNotBlank(mtlId)) {
            lambdaQueryWrapper.eq(PurchaseMtl::getMtlId, mtlId);
        }

        return baseMapper.selectList(lambdaQueryWrapper);
    }
}
