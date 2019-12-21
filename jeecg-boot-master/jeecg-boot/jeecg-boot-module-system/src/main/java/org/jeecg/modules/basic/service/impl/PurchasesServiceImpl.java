package org.jeecg.modules.basic.service.impl;

import org.jeecg.modules.basic.entity.Purchases;
import org.jeecg.modules.basic.mapper.PurchasesMapper;
import org.jeecg.modules.basic.service.IPurchasesService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 采购列表
 * @Author: jeecg-boot
 * @Date:   2019-12-14
 * @Version: V1.0
 */
@Service
public class PurchasesServiceImpl extends ServiceImpl<PurchasesMapper, Purchases> implements IPurchasesService {

}
