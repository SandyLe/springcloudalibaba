package org.jeecg.modules.Allot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.inventory.entity.AllotDtl;
import org.jeecg.modules.inventory.mapper.AllotDtlMapper;
import org.jeecg.modules.inventory.service.AllotDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AllotDtlServiceImpl extends ServiceImpl<AllotDtlMapper, AllotDtl>  implements AllotDtlService {

    @Autowired
    private AllotDtlMapper allotDtlMapper;
}
