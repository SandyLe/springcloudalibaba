package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.inventory.entity.Allot;
import org.jeecg.modules.inventory.mapper.AllotMapper;
import org.jeecg.modules.inventory.service.AllotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AllotServiceImpl extends ServiceImpl<AllotMapper, Allot>  implements AllotService {

    @Autowireda
    private AllotMapper llotMapper;

}
