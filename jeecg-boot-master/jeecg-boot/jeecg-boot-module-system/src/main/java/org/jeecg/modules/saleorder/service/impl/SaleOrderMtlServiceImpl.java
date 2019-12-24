package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.mapper.SaleOrderMtlMapper;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleOrderMtlServiceImpl extends ServiceImpl<SaleOrderMtlMapper, SaleOrderMtl>  implements SaleOrderMtlService {

    @Autowired
    private SaleOrderMtlMapper saleOrderMtlMapper;

}
