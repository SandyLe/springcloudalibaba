package org.jeecg.modules.combind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.combind.entity.TeardownDtl;
import org.jeecg.modules.combind.mapper.TeardownDtlMapper;
import org.jeecg.modules.combind.service.TeardownDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
@Service
public class TeardownDtlServiceImpl extends ServiceImpl<TeardownDtlMapper, TeardownDtl> implements TeardownDtlService {

    @Autowired
    private TeardownDtlMapper teardownDtlMapper;

}
