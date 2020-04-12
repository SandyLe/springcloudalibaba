package org.jeecg.modules.repair.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.repair.entity.RepairOrderDtl;
import org.jeecg.modules.repair.mapper.RepairOrderDtlMapper;
import org.jeecg.modules.repair.service.RepairOrderDtlService;
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
public class RepairOrderDtlServiceImpl extends ServiceImpl<RepairOrderDtlMapper, RepairOrderDtl> implements RepairOrderDtlService {

    @Autowired
    private RepairOrderDtlMapper repairOrderDtlMapper;

    @Override
    public void deleteBySourceId(String sourceId) {
        LambdaQueryWrapper<RepairOrderDtl> queryWrapper = new LambdaQueryWrapper<RepairOrderDtl>().eq(RepairOrderDtl::getSourceId, sourceId);
        repairOrderDtlMapper.delete(queryWrapper);
    }

    @Override
    public void deleteBySourceIds(List<String> sourceIdList) {
        LambdaQueryWrapper<RepairOrderDtl> queryWrapper = new LambdaQueryWrapper<RepairOrderDtl>().in(RepairOrderDtl::getSourceId, sourceIdList);
        repairOrderDtlMapper.delete(queryWrapper);
    }

    @Override
    public List<RepairOrderDtl> findBySourceId(String sourceId) {
        LambdaQueryWrapper<RepairOrderDtl> queryWrapper = new LambdaQueryWrapper<RepairOrderDtl>().eq(RepairOrderDtl::getSourceId, sourceId);
        return repairOrderDtlMapper.selectList(queryWrapper);
    }
}
