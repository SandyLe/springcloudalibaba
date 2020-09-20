package org.jeecg.modules.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.basic.entity.Supplementary;

public interface SupplementaryService extends IService<Supplementary> {

    /**
     * 根据Code查询
     * @param companyId
     * @param code
     * @return
     */
    public Supplementary findByCcIdCode (String companyId, String code);
}
