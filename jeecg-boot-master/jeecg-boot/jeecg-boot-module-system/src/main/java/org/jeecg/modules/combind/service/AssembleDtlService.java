package org.jeecg.modules.combind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.combind.entity.AssembleDtl;

import java.util.List;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface AssembleDtlService extends IService<AssembleDtl> {

    public void deleteBySourceId (String sourceId);
    public void deleteBySourceIds (List<String> sourceIdList);
    public List<AssembleDtl> findBySourceId (String sourceId);
}
