package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;

import java.math.BigDecimal;
import java.util.List;

public interface SaleOrderMtlService extends IService<SaleOrderMtl> {

    /**
     * 保存销售产品
     * @param saleOrderMtl
     * @return
     */
    public BigDecimal saveSaleOrderMtl(SaleOrderMtl saleOrderMtl);

    /**
     * 获取销售单产品
     * @param sourceId
     * @return
     */
    List<SaleOrderMtl> findList(String sourceId);

    /**
     * 汇总数量
     * @param sourceId
     * @return
     */
    BigDecimal sumBySourceId (String sourceId);
}
