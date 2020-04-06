package org.jeecg.modules.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.mapper.PurchaseMapper;
import org.jeecg.modules.purchase.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2019/12/27 16:15
 * @Version: V1.0
 */
@Service
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase> implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public List<Purchase> findByBatchNo(String batchNo) {
        LambdaQueryWrapper<Purchase> lambdaQueryWrapper = new LambdaQueryWrapper<Purchase>().eq(Purchase::getBatchNo, batchNo);
        return purchaseMapper.selectList(lambdaQueryWrapper);
    }
}
