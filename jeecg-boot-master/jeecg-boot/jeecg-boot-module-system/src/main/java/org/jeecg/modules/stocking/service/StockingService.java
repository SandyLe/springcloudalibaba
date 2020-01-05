package org.jeecg.modules.stocking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.stocking.entity.Stocking;

public interface StockingService extends IService<Stocking> {

    public String handleStocking (String id);
}
