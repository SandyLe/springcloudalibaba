package org.jeecg.modules.taobao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.taobao.entity.PrintOrderAssignment;
import org.jeecg.modules.taobao.mapper.PrintOrderAssignmentMapper;
import org.jeecg.modules.taobao.service.PrintOrderAssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrintOrderAssignmentServiceImpl extends ServiceImpl<PrintOrderAssignmentMapper, PrintOrderAssignment> implements PrintOrderAssignmentService {

    @Override
    public boolean savePrintOrderAssignment(PrintOrderAssignment entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean savePrintOrderAssignmentBatch(List<PrintOrderAssignment> printOrderAssignmentList) {
        return super.saveOrUpdateBatch(printOrderAssignmentList);
    }

    @Override
    public PrintOrderAssignment findByTid(String tid) {

        return getOne(new LambdaQueryWrapper<PrintOrderAssignment>().eq(PrintOrderAssignment::getTid, tid));
    }
}
