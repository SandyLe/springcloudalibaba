package org.jeecg.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.inventory.dto.AllotDto;
import org.jeecg.modules.inventory.entity.Allot;
import org.jeecg.modules.inventory.entity.AllotDtl;

import java.util.List;

public interface AllotService extends IService<Allot> {

    public String saveOrder(AllotDto allotDto);
    public String editOrder(AllotDto allotDto);
}
