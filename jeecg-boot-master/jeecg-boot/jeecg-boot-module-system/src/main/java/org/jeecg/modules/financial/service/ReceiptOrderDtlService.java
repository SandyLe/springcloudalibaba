package org.jeecg.modules.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.financial.entity.ReceiptOrderDtl;

public interface ReceiptOrderDtlService extends IService<ReceiptOrderDtl> {

    @Override
    boolean saveOrUpdate(ReceiptOrderDtl entity);
}
