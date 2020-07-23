package org.jeecg.modules.combind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.combind.dto.AssembleDto;
import org.jeecg.modules.combind.entity.Assemble;
import org.jeecg.modules.combind.entity.AssembleDtl;
import org.jeecg.modules.combind.mapper.AssembleMapper;
import org.jeecg.modules.combind.service.AssembleDtlService;
import org.jeecg.modules.combind.service.AssembleService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.mapper.PurchaseMapper;
import org.jeecg.modules.purchase.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
@Service
public class AssembleServiceImpl extends ServiceImpl<AssembleMapper, Assemble> implements AssembleService {

    @Autowired
    private AssembleMapper assembleMapper;
    @Autowired
    private AssembleDtlService assembleDtlService;
    @Lazy
    @Autowired
    private InventoryInService inventoryInService;
    @Lazy
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    @Transactional
    public String saveOrder(AssembleDto assembledto){
        // 组装主表
        String code = billCodeBuilderService.getBillCode(BillType.ASSEMBLE.getId());
        assembledto.setCode(code);
        assembledto.setBillStatus(BillStatus.NEW.getId());
        super.save(assembledto);

        //组装单子表
        if (CollectionUtils.isNotEmpty(assembledto.getDetaillist())){
            List<AssembleDtl> mtls = assembledto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //组装商品详情
                o.setCode(code);
                o.setCompanyId(assembledto.getCompanyId());
                o.setSourceId(assembledto.getId());
            });
            assembleDtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(assembledto.getWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setCompanyId(assembledto.getCompanyId());
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(assembledto.getWarehouseId());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(assembledto.getId());
            inventoryIn.setBillType(BillType.INVENTORYIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.ASSEMBLE.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        if (StringUtils.isNotBlank(assembledto.getWarehouseId())) {
            // 出库
            InventoryOut inventoryOut = new InventoryOut(assembledto.getId(), assembledto.getCode(), BillType.INVENTORYOUT.getId(), BillType.ASSEMBLE.getId(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setCompanyId(assembledto.getCompanyId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return assembledto.getId();
    }

    @Override
    @Transactional
    public String editOrder(AssembleDto assembledto){

        // 组装主表
        super.updateById(assembledto);
        if (assembledto.getDetaillist().size() > 0){
            for (AssembleDtl item: assembledto.getDetaillist()){
                item.setCompanyId(assembledto.getCompanyId());
                //组装商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    assembleDtlService.updateById(item);
                else{
                    item.setSourceId(assembledto.getId());
                    assembleDtlService.save(item);
                }
            }
        }

        // 入库单主表
        inventoryInService.deleteBySourceId(assembledto.getId());

        if (StringUtils.isNotBlank(assembledto.getWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setCompanyId(assembledto.getCompanyId());
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(assembledto.getWarehouseId());
            inventoryIn.setSourceCode(assembledto.getCode());
            inventoryIn.setSourceId(assembledto.getId());
            inventoryIn.setBillType(BillType.INVENTORYIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.ASSEMBLE.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        inventoryOutService.deleteBySourceId(assembledto.getBillType(), assembledto.getId());
        if (StringUtils.isNotBlank(assembledto.getWarehouseId())) {
            // 配件出库
            InventoryOut inventoryOut = new InventoryOut(assembledto.getId(), assembledto.getCode(), BillType.INVENTORYOUT.getId(), BillType.ASSEMBLE.getId(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setCompanyId(assembledto.getCompanyId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return assembledto.getId();
    }
}
