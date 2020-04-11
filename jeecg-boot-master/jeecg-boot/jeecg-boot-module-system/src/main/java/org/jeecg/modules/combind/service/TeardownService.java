package org.jeecg.modules.combind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.combind.dto.AssembleDto;
import org.jeecg.modules.combind.dto.TeardownDto;
import org.jeecg.modules.combind.entity.Teardown;

import java.util.List;
/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface TeardownService extends IService<Teardown> {

    public String saveOrder(TeardownDto teardowndto);
    public String editOrder(TeardownDto teardownDto);
}
