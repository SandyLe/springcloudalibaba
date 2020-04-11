package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.inventory.entity.Allot;
import org.jeecg.modules.inventory.mapper.AllotMapper;
import org.jeecg.modules.inventory.service.AllotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AllotServiceImpl extends ServiceImpl<AllotMapper, Allot>  implements AllotService {

    @Autowired
    private AllotMapper allotMapper;
    @Autowired
    private AllotDtlService allotDtlService;
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;


    @Override
    @Transactional
    public String saveOrder (AllotDto allotdto) {
        // 调拨单主表
        String code = billCodeBuilderService.getBillCode(BillType.ALLOT.getId());
        allotdto.setCode(code);
        super.save(allotdto);

        //调拨单子表
        if (CollectionUtils.isNotEmpty(allotdto.getDetaillist())){
            List<AllotDtl> mtls = allotdto.getDetaillist().stream().filter(o->StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //调拨商品详情
                o.setCode(code);
                o.setSourceId(allotdto.getId());
            });
            allotDtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(allotdto.getToWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(allotdto.getToWarehouseId());
            inventoryIn.setPutInTime(new Date());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(allotdto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.ALLOT.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }
        if (StringUtils.isNotBlank(allotdto.getFromWarehouseId())) {
            // 销售出库
            InventoryOut inventoryOut = new InventoryOut(allotdto.getId(), allotdto.getCode(), BillType.STOREOUT.getId(), BillType.ALLOT.getId(), allotdto.getFromWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return allotdto.getId();
    }

    @Override
    @Transactional
    public String editOrder (AllotDto allotdto) {
        // 调拨主表
        super.updateById(allotdto);
        if (allotdto.getDetaillist().size() > 0){
            for (AllotDtl item: allotdto.getDetaillist()){
                //调拨商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    allotDtlService.updateById(item);
                else{
                    item.setSourceId(allotdto.getId());
                    allotDtlService.save(item);
                }
            }
        }

        // 入库单主表
        inventoryInService.deleteBySourceId(allotdto.getId());
        if (StringUtils.isNotBlank(allotdto.getFromWarehouseId())) {
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(allotdto.getFromWarehouseId());
            inventoryIn.setPutInTime(new Date());
            inventoryIn.setSourceCode(allotdto.getCode());
            inventoryIn.setSourceId(allotdto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.PURCHASEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        inventoryOutService.deleteBySourceId(allotdto.getId());
        if (StringUtils.isNotBlank(allotdto.getFromWarehouseId())) {
            // 销售出库
            InventoryOut inventoryOut = new InventoryOut(allotdto.getId(), allotdto.getCode(), BillType.STOREOUT.getId(), BillType.ALLOT.getId(), allotdto.getFromWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOul�8���la�}�7�~�GyWآɐ�t�<�͆mO�+[ ���
;���A�O�`�쫂�2G��E�.�F���_(.��:��u6j���Kr�J�	�Zû��������V�z�c��</��'�޾