package org.jeecg.modules.repair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.repair.entity.RepairOrderDtl;

import java.util.List;


/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface RepairOrderDtlService extends IService<RepairOrderDtl> {

    public void deleteBySourceId(String sourceId);
    public void deleteBySourceIds(List<String> sourceIdList);
    public List<RepairOrderDtl> findBySourceId(String sourceId);
}
