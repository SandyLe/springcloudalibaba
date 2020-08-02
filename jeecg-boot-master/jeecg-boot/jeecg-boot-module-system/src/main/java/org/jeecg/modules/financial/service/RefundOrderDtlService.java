package org.jeecg.modules.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.financial.entity.RefundOrderDtl;

public interface RefundOrderDtlService extends IService<RefundOrderDtl> {

    @Override
    boolean saveOrUpdate(RefundOrderDtl entity);

    /**
     * 更新状态
     * @param entity
     */
    public void updateBillStatus(RefundOrderDtl entity);
}
