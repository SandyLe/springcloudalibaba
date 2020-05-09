package org.jeecg.modules.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.workorder.entity.WorkOrderDtl;

import java.util.List;


/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface WorkOrderDtlService extends IService<WorkOrderDtl> {

    public void deleteBySourceId(String sourceId);
    public void deleteBySourceIds(List<String> sourceIdList);
    public List<WorkOrderDtl> findBySourceId(String sourceId);
}
