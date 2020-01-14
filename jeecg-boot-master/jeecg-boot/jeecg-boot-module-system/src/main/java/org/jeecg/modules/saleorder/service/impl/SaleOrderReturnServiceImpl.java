package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.saleorder.entity.SaleOrderReturn;
import org.jeecg.modules.saleorder.mapper.SaleOrderReturnMapper;
import org.jeecg.modules.saleorder.service.SaleOrderReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleOrderReturnServiceImpl extends ServiceImpl<SaleOrderReturnMapper, SaleOrderReturn>  implements SaleOrderReturnService {

    @Autowired
    private SaleOrderReturnMapper saleOrderReturnMapper;

}
