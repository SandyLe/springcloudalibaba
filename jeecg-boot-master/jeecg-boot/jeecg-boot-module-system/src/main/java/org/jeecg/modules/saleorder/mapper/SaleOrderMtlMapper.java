package org.jeecg.modules.saleorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;

import java.math.BigDecimal;

public interface SaleOrderMtlMapper extends BaseMapper<SaleOrderMtl> {

    public BigDecimal sumBySourceId (@Param("sourceId")String sourceId);
}
