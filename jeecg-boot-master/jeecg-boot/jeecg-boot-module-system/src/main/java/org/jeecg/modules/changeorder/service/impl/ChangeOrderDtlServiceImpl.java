package org.jeecg.modules.changeorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.changeorder.entity.ChangeOrderDtl;
import org.jeecg.modules.changeorder.mapper.ChangeOrderDtlMapper;
import org.jeecg.modules.changeorder.service.ChangeOrderDtlService;
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
public class ChangeOrderDtlServiceImpl extends ServiceImpl<ChangeOrderDtlMapper, ChangeOrderDtl> implements ChangeOrderDtlService {

    @Autowired
    private ChangeOrderDtlMapper changeOrderDtlMapper;

    @Override
    public void deleteBySourceId(String sourceId) {
        LambdaQueryWrapper<ChangeOrderDtl> queryWrapper = new LambdaQueryWrapper<ChangeOrderDtl>().eq(ChangeOrderDtl::getSourceId, sourceId);
        changeOrderDtlMapper.delete(queryWrapper);
    }

    @Override
    public void deleteBySourceIds(List<String> sourceIdList) {
        LambdaQueryWrapper<ChangeOrderDtl> queryWrapper = new LambdaQueryWrapper<ChangeOrderDtl>().in(ChangeOrderDtl::getSourceId, sourceIdList);
        changeOrderDtlMapper.delete(queryWrapper);
    }

    @Override
    public List<ChangeOrderDtl> findBySourceId(String sourceId) {
        LambdaQueryWrapper<ChangeOrderDtl> queryWrapper = new LambdaQueryWrapper<ChangeOrderDtl>().eq(ChangeOrderDtl::getSourceId, sourceId);
        return changeOrderDtlMapper.selectList(queryWrapper);
    }
}
