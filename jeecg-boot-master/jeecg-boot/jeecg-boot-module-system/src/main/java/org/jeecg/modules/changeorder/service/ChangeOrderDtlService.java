package org.jeecg.modules.changeorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.changeorder.entity.ChangeOrderDtl;

import java.util.List;


/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface ChangeOrderDtlService extends IService<ChangeOrderDtl> {

    public void deleteBySourceId(String sourceId);
    public void deleteBySourceIds(List<String> sourceIdList);
    public List<ChangeOrderDtl> findBySourceId(String sourceId);
}
