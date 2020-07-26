package org.jeecg.modules.invoice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.invoice.entity.InvoiceAddress;

public interface InvoiceAddressService extends IService<InvoiceAddress> {

    public InvoiceAddress findBySouorceId(String sourceId);

}
