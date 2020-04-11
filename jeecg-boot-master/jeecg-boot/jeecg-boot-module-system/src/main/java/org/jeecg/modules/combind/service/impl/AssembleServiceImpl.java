package org.jeecg.modules.combind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.combind.dto.AssembleDto;
import org.jeecg.modules.combind.entity.Assemble;
import org.jeecg.modules.combind.mapper.AssembleMapper;
import org.jeecg.modules.combind.service.AssembleService;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.mapper.PurchaseMapper;
import org.jeecg.modules.purchase.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private InventoryInService inventoryInService;
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
        super.save(assembledto);

        //组装单子表
        if (CollectionUtils.isNotEmpty(assembledto.getDetaillist())){
            List<AssembleDtl> mtls = assembledto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //组装商品详情
                o.setCode(code);
                o.setSourceId(assembledto.getId());
            });
            assembleDtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(assembledto.getWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(assembledto.getWarehouseId());
            inventoryIn.setPutInTime(new Date());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(assembledto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.ASSEMBLE.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        if (StringUtils.isNotBlank(allotdto.getFromWarehouseId())) {
            // 销售出库
            InventoryOut inventoryOut = new InventoryOut(allotdto.getId(), allotdto.getCode(), BillType.STOREOUT.getId(), BillType.ALLOT.getId(), allotdto.getFromWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
    }

    @Override
    @Transactional
    public String editOrder(AssembleDto assembledto){

        // 组装主表
        assembleService.updateById(assembledto);
        if (assembledto.getDetaillist().size() > 0){
            for (AssembleDtl item: assembledto.getDetaillist()){
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
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(assembledto.getWarehouseId());
            inventoryIn.setPutInTime(new Date());
            inventoryIn.setSourceCode(assembledto.getCode());
            inventoryIn.setSourceId(assembledto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.ASSEMBLE.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        inventoryOutService.deleteBySourceId(assembledto.getId());
        if (StringUtils.isNotBlank(assembledto.getFromWarehouseId())) {
            // 销售出库
            InventoryOut inventoryOut = new InventoryOut(assembledto.getId(), assembledto.getCode(), BillType.STOREOUT.getId(), BillType.ASSEMBLE.getId(), assembledto.getFromWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            invlHQ�|�Ę�d�q���ڲ\lh��n�Uc�5O�C�S��<��O�۪��Hl��jߡ�5��D�ά/�Dr��