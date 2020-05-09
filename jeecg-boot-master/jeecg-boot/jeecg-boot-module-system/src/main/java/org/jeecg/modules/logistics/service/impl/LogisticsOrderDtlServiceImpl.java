package org.jeecg.modules.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.logistics.entity.LogisticsOrderDtl;
import org.jeecg.modules.logistics.mapper.LogisticsOrderDtlMapper;
import org.jeecg.modules.logistics.service.LogisticsOrderDtlService;
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
public class LogisticsOrderDtlServiceImpl extends ServiceImpl<LogisticsOrderDtlMapper, LogisticsOrderDtl> implements LogisticsOrderDtlService {

    @Autowired
    private LogisticsOrderDtlMapper changeOrderDtlMapper;

    @Override
    public void deleteBySourceId(String sourceId) {
        LambdaQueryWrapper<LogisticsOrderDtl> queryWrapper = new LambdaQueryWrapper<LogisticsOrderDtl>().eq(LogisticsOrderDtl::getSourceId, sourceId);
        changeOrderDtlMapper.delete(queryWrapper);
    }

    @Override
    public void deleteBySourceIds(List<String> sourceIdList) {
        LambdaQueryWrapper<LogisticsOrderDtl> queryWrapper = new LambdaQueryWrapper<LogisticsOrderDtl>().in(LogisticsOrderDtl::getSourceId, sourceIdList);
        changeOrderDtlMapper.delete(queryWrapper);
    }

    @Override
    public List<LogisticsOrderDtl> findBySourceId(String sourceId) {
        LambdaQueryWrapper<LogisticsOrderDtl> queryWrapper = new LambdaQueryWrapper<LogisticsOrderDtl>().eq(LogisticsOrderDtl::getSourceId, sourceId);
        return changeOrderDtlMapper.selectList(queryWrapper);
    }
}
