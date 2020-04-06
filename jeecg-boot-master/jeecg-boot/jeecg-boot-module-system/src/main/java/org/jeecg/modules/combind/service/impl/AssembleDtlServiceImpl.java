package org.jeecg.modules.combind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.combind.entity.AssembleDtl;
import org.jeecg.modules.combind.mapper.AssembleDtlMapper;
import org.jeecg.modules.combind.service.AssembleDtlService;
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
public class AssembleDtlServiceImpl extends ServiceImpl<AssembleDtlMapper, AssembleDtl> implements AssembleDtlService {

    @Autowired
    private AssembleDtlMapper assembleDtlMapper;

}
