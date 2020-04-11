package org.jeecg.modules.combind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.combind.dto.AssembleDto;
import org.jeecg.modules.combind.entity.Assemble;
import org.jeecg.modules.purchase.entity.Purchase;

import java.util.List;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface AssembleService extends IService<Assemble> {

    public String saveOrder(AssembleDto assembledto);
    public String editOrder(AssembleDto assembledto);
}
