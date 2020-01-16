package org.jeecg.modules.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.purchase.entity.PurchaseReturn;
import org.jeecg.modules.purchase.mapper.IPurchaseReturnMapper;
import org.jeecg.modules.purchase.service.IPurchaseReturnService;
import org.springframework.stereotype.Service;

@Service
public class PurchaseReturnServiceImpl extends ServiceImpl<IPurchaseReturnMapper, PurchaseReturn> implements IPurchaseReturnService {
}
