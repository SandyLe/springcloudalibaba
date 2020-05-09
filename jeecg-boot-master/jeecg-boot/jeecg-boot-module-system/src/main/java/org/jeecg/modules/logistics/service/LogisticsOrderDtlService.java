package org.jeecg.modules.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.logistics.entity.LogisticsOrderDtl;

import java.util.List;


/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface LogisticsOrderDtlService extends IService<LogisticsOrderDtl> {

    public void deleteBySourceId(String sourceId);
    public void deleteBySourceIds(List<String> sourceIdList);
    public List<LogisticsOrderDtl> findBySourceId(String sourceId);
}
