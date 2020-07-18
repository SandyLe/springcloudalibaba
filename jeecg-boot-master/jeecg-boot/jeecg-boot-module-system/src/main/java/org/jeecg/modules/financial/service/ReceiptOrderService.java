package org.jeecg.modules.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.financial.entity.ReceiptOrder;

public interface ReceiptOrderService extends IService<ReceiptOrder> {

    /**
     * 生成收款单
     * @param sourceId
     * @param sourceBillType
     * @param ccId
     * @return
     */
    public String createReceipt(String sourceId, Integer sourceBillType, String ccId);

    /**
     * 查询收款单
     * @param sourceBillId
     * @param sourceBillType
     * @param companyId
     * @return
     */
    public ReceiptOrder findBySourceBillId(String sourceBillId,Integer sourceBillType, String companyId);
}
