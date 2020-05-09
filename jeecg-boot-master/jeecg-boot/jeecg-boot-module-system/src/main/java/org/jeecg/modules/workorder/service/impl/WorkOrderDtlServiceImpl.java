package org.jeecg.modules.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.workorder.entity.WorkOrderDtl;
import org.jeecg.modules.workorder.mapper.WorkOrderDtlMapper;
import org.jeecg.modules.workorder.service.WorkOrderDtlService;
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
public class WorkOrderDtlServiceImpl extends ServiceImpl<WorkOrderDtlMapper, WorkOrderDtl> implements WorkOrderDtlService {

    @Autowired
    private WorkOrderDtlMapper workOrderDtlMapper;

    @Override
    public void deleteBySourceId(String sourceId) {
        LambdaQueryWrapper<WorkOrderDtl> queryWrapper = new LambdaQueryWrapper<WorkOrderDtl>().eq(WorkOrderDtl::getSourceId, sourceId);
        workOrderDtlMapper.delete(queryWrapper);
    }

    @Override
    public void deleteBySourceIds(List<String> sourceIdList) {
        LambdaQueryWrapper<WorkOrderDtl> queryWrapper = new LambdaQueryWrapper<WorkOrderDtl>().in(WorkOrderDtl::getSourceId, sourceIdList);
        workOrderDtlMapper.delete(queryWrapper);
    }

    @Override
    public List<WorkOrderDtl> findBySourceId(String sourceId) {
        LambdaQueryWrapper<WorkOrderDtl> queryWrapper = new LambdaQueryWrapper<WorkOrderDtl>().eq(WorkOrderDtl::getSourceId, sourceId);
        return workOrderDtlMapper.selectList(queryWrapper);
    }
}
