package org.jeecg.modules.purchase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.purchase.entity.Purchase;

import java.util.List;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2019/12/27 16:13
 * @Version: V1.0
 */
public interface PurchaseService extends IService<Purchase> {

    public List<Purchase> findByBatchNo(String batchNo);
}
