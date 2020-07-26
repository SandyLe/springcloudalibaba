package org.jeecg.modules.invoice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.invoice.entity.Invoice;

public interface InvoiceService extends IService<Invoice> {

    public Invoice findBySouorceId(String sourceId);
}
