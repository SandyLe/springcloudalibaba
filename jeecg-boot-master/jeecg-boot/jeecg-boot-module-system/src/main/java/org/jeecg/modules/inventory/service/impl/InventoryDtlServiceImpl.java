package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.inventory.entity.InventoryDtl;
import org.jeecg.modules.inventory.mapper.InventoryDtlMapper;
import org.jeecg.modules.inventory.service.InventoryDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryDtlServiceImpl extends ServiceImpl<InventoryDtlMapper, InventoryDtl>  implements InventoryDtlService {

    @Autowired
    private InventoryDtlMapper inventoryDtlMapper;

    @Override
    public InventoryDtl findInventoryDtl(String warehouseId, String batchNo, String mtlId, String unitId, String auxiliaryId) {

        LambdaQueryWrapper<InventoryDtl> queryWrapper = new LambdaQueryWrapper<InventoryDtl>();
        queryWrapper.eq(InventoryDtl::getWarehouseId, warehouseId);
        queryWrapper.eq(InventoryDtl::getMtlId, mtlId);
        queryWrapper.eq(InventoryDtl::getBatchNo, batchNo);
        if (StringUtils.isNotBlank(unitId)) {
            queryWrapper.eq(InventoryDtl::getUnitId, unitId);
        }
        if (StringUtils.isNotBlank(auxiliaryId)) {
            queryWrapper.eq(InventoryDtl::getAuxiliaryId, auxiliaryId);
        }
        return inventoryDtlMapper.selectOne(queryWrapper);
    }

    @Override
    public List<InventoryDtl> findAvailableIntoryDtlList(String warehouseId, String mtlId, String unitId, String auxiliaryId) {
        LambdaQueryWrapper<InventoryDtl> lambdaQueryWrapper = new LambdaQueryWrapper<InventoryDtl>();
        lambdaQueryWrapper.eq(InventoryDtl::getWarehouseId, warehouseId);
        lambdaQueryWrapper.eq(InventoryDtl::getMtlId, mtlId);
        lambdaQueryWrapper.eq(InventoryDtl::getUnitId, unitId);
        lambdaQueryWrapper.lt(InventoryDtl::getAvailableAmount, BigDecimal.ZERO);
        if (StringUtils.isNotBlank(auxiliaryId)) {
            lambdaQueryWrapper.eq(InventoryDtl::getAuxiliaryId, auxiliaryId);
        }
        lambdaQueryWrapper.orderByAsc(InventoryDtl::getCreateTime);
        return inventoryDtlMapper.selectList(lambdaQueryWrapper);
    }

}
