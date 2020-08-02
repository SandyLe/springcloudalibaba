package org.jeecg.modules.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.financial.entity.RefundOrder;

public interface RefundOrderService extends IService<RefundOrder> {


    @Override
    public boolean saveOrUpdate(RefundOrder refundOrder);

    /**
     * 查询退款单
     * @param sourceBillId
     * @param sourceBillType
     * @param companyId
     * @return
     */
    public RefundOrder findBySourceBillId(String sourceBillId, Integer sourceBillType, String companyId);
}
