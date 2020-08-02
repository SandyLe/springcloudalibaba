package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.inventory.dto.AllotDto;
import org.jeecg.modules.inventory.entity.Allot;
import org.jeecg.modules.inventory.entity.AllotDtl;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.mapper.AllotMapper;
import org.jeecg.modules.inventory.service.AllotDtlService;
import org.jeecg.modules.inventory.service.AllotService;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllotServiceImpl extends ServiceImpl<AllotMapper, Allot>  implements AllotService {

    @Autowired
    private AllotMapper allotMapper;
    @Autowired
    private AllotDtlService allotDtlService;
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
    public String saveOrder(AllotDto allotdto) {
        // 调拨单主表
        String code = billCodeBuilderService.getBillCode(BillType.ALLOT.getId());
        allotdto.setCode(code);
        allotdto.setBillStatus(BillStatus.NEW.getId());
        super.save(allotdto);

        //调拨单子表
        if (CollectionUtils.isNotEmpty(allotdto.getDetaillist())) {
            List<AllotDtl> mtls = allotdto.getDetaillist().stream().filter(o -> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o -> {
                //调拨商品详情
                o.setCompanyId(allotdto.getCompanyId());
                o.setCode(code);
                o.setSourceId(allotdto.getId());
            });
            allotDtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(allotdto.getToWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setCompanyId(allotdto.getCompanyId());
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(allotdto.getToWarehouseId());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(allotdto.getId());
            inventoryIn.setBillType(BillType.INVENTORYIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.ALLOT.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
            inventoryIn.setWarehouseId(allotdto.getToWarehouseId());
            inventoryInService.saveToInventoryIn(inventoryIn);
        }
        if (StringUtils.isNotBlank(allotdto.getFromWarehouseId())) {
            // 销售出库
            InventoryOut inventoryOut = new InventoryOut(allotdto.getId(), allotdto.getCode(), BillType.INVENTORYOUT.getId(), BillType.ALLOT.getId(),  BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setCompanyId(allotdto.getCompanyId());
            inventoryOut.setWarehouseId(allotdto.getFromWarehouseId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return allotdto.getId();
    }

    @Override
    @Transactional
    public String editOrder(AllotDto allotdto) {
        // 调拨主表
        super.updateById(allotdto);
        if (allotdto.getDetaillist().size() > 0) {
            for (AllotDtl item : allotdto.getDetaillist()) {
                item.setCompanyId(allotdto.getCompanyId());
                //调拨商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    allotDtlService.updateById(item);
                else {
                    item.setSourceId(allotdto.getId());
                    allotDtlService.save(item);
                }
            }
        }

        // 入库单主表
        inventoryInService.deleteBySourceId(allotdto.getBillType(), allotdto.getId());
        if (StringUtils.isNotBlank(allotdto.getFromWarehouseId())) {
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setCompanyId(allotdto.getCompanyId());
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(allotdto.getFromWarehouseId());
            inventoryIn.setSourceCode(allotdto.getCode());
            inventoryIn.setSourceId(allotdto.getId());
            inventoryIn.setBillType(BillType.INVENTORYIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.ALLOT.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        inventoryOutService.deleteBySourceId(allotdto.getBillType(), allotdto.getId());
        if (StringUtils.isNotBlank(allotdto.getFromWarehouseId())) {
            // 销售出库
            InventoryOut inventoryOut = new InventoryOut(allotdto.getId(), allotdto.getCode(), BillType.INVENTORYOUT.getId(), BillType.ALLOT.getId(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setCompanyId(allotdto.getCompanyId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return allotdto.getId();
    }
}
