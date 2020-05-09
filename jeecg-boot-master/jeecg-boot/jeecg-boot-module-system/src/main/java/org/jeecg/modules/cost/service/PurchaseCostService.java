package org.jeecg.modules.cost.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cost.entity.PurchaseCost;

public interface PurchaseCostService extends IService<PurchaseCost> {

    public PurchaseCost findMtlPurchaseCost (String mtlId, String unitId, String batchNo);

}
