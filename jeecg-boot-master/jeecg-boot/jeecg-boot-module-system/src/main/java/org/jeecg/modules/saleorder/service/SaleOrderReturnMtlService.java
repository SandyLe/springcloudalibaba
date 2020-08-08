package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.entity.SaleOrderReturn;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnMtl;

import java.math.BigDecimal;
import java.util.List;

public interface SaleOrderReturnMtlService extends IService<SaleOrderReturnMtl> {

    /**
     * 保存销售退货单产品
     * @param saleOrderReturnMtl
     * @return
     */
    public BigDecimal saveSaleOrderReturnMtl(SaleOrderReturnMtl saleOrderReturnMtl);

    /**
     * 获取列表
     * @param souceId
     * @return
     */
    public List<SaleOrderReturnMtl> getList(String souceId);

    /**
     * 汇总数量
     * @param sourceId
     * @return
     */
    BigDecimal sumBySourceId (List<String> sourceId);
}
