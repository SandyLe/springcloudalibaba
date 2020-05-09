package org.jeecg.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.inventory.entity.AllotDtl;

import java.util.List;

public interface AllotDtlService extends IService<AllotDtl> {

    public void deleteBySourceId (String sourceId);
    public void deleteBySourceIds (List<String> sourceIdList);
    public List<AllotDtl> findBySourceId (String sourceId);
}
