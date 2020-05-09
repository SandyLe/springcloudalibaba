package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.PurchaseBatch;
import org.jeecg.modules.basic.mapper.PurchaseBatchMapper;
import org.jeecg.modules.basic.service.PurchaseBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseBatchServiceImpl extends ServiceImpl<PurchaseBatchMapper, PurchaseBatch> implements PurchaseBatchService {

    @Autowired
    private PurchaseBatchMapper purchaseBatchMapper;
}
