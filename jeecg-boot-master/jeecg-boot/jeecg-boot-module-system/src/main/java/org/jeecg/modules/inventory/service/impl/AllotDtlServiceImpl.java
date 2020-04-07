package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.inventory.entity.AllotDtl;
import org.jeecg.modules.inventory.mapper.AllotDtlMapper;
import org.jeecg.modules.inventory.service.AllotDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllotDtlServiceImpl extends ServiceImpl<AllotDtlMapper, AllotDtl>  implements AllotDtlService {

    @Autowired
    private AllotDtlMapper allotDtlMapper;

    @Override
    public void deleteBySourceId(String sourceId) {
        LambdaQueryWrapper<AllotDtl> queryWrapper = new LambdaQueryWrapper<AllotDtl>().eq(AllotDtl::getSourceId, sourceId);
        allotDtlMapper.delete(queryWrapper);
    }

    @Override
    public void deleteBySourceIds(List<String> sourceIdList) {
        LambdaQueryWrapper<AllotDtl> queryWrapper = new LambdaQueryWrapper<AllotDtl>().in(AllotDtl::getSourceId, sourceIdList);
        allotDtlMapper.delete(queryWrapper);
    }

    @Override
    public List<AllotDtl> findBySourceId(String sourceId) {
        LambdaQueryWrapper<AllotDtl> queryWrapper = new LambdaQueryWrapper<AllotDtl>().eq(AllotDtl::getSourceId, sourceId);
        return allotDtlMapper.selectList(queryWrapper);
    }
}
