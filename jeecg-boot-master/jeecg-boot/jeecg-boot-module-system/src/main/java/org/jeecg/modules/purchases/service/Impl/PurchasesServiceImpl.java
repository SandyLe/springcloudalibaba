package org.jeecg.modules.purchases.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.purchases.entity.Purchases;
import org.jeecg.modules.purchases.mapper.PurchasesMapper;
import org.jeecg.modules.purchases.service.PurchasesService;
import org.springframework.stereotype.Service;


@Service
public class PurchasesServiceImpl extends ServiceImpl<PurchasesMapper,Purchases> implements PurchasesService {
}
