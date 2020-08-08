package org.jeecg.modules.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.workorder.entity.WorkAddress;

public interface WorkAddressService extends IService<WorkAddress> {

    public WorkAddress findBySouorceId(String sourceId);

}
