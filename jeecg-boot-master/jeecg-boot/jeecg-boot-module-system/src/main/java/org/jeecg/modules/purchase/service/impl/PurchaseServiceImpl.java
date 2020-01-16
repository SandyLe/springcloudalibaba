package org.jeecg.modules.purchase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.mapper.IPurchaseMapper;
import org.jeecg.modules.purchase.service.IPurchaseService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2019/12/27 16:15
 * @Version: V1.0
 */
@Service
public class PurchaseServiceImpl extends ServiceImpl<IPurchaseMapper, Purchase> implements IPurchaseService {
}
