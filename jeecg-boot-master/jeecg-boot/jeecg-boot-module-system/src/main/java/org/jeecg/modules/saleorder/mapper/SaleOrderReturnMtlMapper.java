package org.jeecg.modules.saleorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnMtl;

import java.math.BigDecimal;
import java.util.List;

public interface SaleOrderReturnMtlMapper extends BaseMapper<SaleOrderReturnMtl> {

    public BigDecimal sumBySourceId (@Param("sourceId") List<String> sourceId);
}
