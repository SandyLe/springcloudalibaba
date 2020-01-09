package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.BillCodeBuilder;
import org.jeecg.modules.basic.mapper.BillCodeBuilderMapper;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillCodeBuilderServiceImpl extends ServiceImpl<BillCodeBuilderMapper, BillCodeBuilder> implements BillCodeBuilderService{

    @Autowired
    private BillCodeBuilderMapper billCodeBuilderMapper;

    @Override
    public String getBillCode(Integer billType) {
        return null;
    }
}
