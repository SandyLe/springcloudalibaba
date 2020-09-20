package org.jeecg.modules.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.basic.entity.SupplementaryValue;

public interface SupplementaryValueService extends IService<SupplementaryValue> {

    /**
     * 根据编码查询
     * @param sourceId
     * @param code
     * @return
     */
    public SupplementaryValue findBySourceIdCode (String sourceId, String code);
}
