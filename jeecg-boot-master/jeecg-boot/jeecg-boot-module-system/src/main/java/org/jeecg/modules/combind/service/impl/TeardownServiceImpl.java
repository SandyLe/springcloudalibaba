package org.jeecg.modules.combind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.combind.dto.TeardownDto;
import org.jeecg.modules.combind.entity.Teardown;
import org.jeecg.modules.combind.entity.TeardownDtl;
import org.jeecg.modules.combind.mapper.TeardownMapper;
import org.jeecg.modules.combind.service.TeardownDtlService;
import org.jeecg.modules.combind.service.TeardownService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
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

import java.util.ArrayList;
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
public class TeardownServiceImpl extends ServiceImpl<TeardownMapper, Teardown> implements TeardownService {

    @Autowired
    private TeardownMapper teardownMapper;
    @Autowired
    private TeardownDtlService teardownDtlService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    @Lazy
    @Autowired
    private InventoryInService inventoryInService;
    @Lazy
    @Autowired
    private InventoryOutService inventoryOutService;

    @Override
    @Transactional
    public String saveOrder(TeardownDto teardownDto){
        // 拆卸主表
        String code = billCodeBuilderService.getBillCode(BillType.TEARDOWN.getId());
        teardownDto.setCode(code);
        teardownDto.setBillStatus(BillStatus.NEW.getId());
        super.save(teardownDto);

        //拆卸单子表
        List<InventoryInMtl> detaillist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(teardownDto.getDetaillist())){
            List<TeardownDtl> mtls = teardownDto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //拆卸商品详情
                o.setCode(code);
                o.setCompanyId(teardownDto.getCompanyId());
                o.setSourceId(teardownDto.getId());
            });
            teardownDtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(teardownDto.getWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setCompanyId(teardownDto.getCompanyId());
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(teardownDto.getWarehouseId());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(teardownDto.getId());
            inventoryIn.setBillType(BillType.INVENTORYIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.TEARDOWN.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }
        if (StringUtils.isNotBlank(teardownDto.getWarehouseId())) {
            // 拆卸出库
            InventoryOut inventoryOut = new InventoryOut(teardownDto.getId(), teardownDto.getCode(), BillType.INVENTORYOUT.getId(), BillType.TEARDOWN.getId(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setCompanyId(teardownDto.getCompanyId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return teardownDto.getId();
    }

    @Override
    @Transactional
    public String editOrder(TeardownDto teardowndto){
        // 拆卸主表
        super.updateById(teardowndto);
        if (teardowndto.getDetaillist().size() > 0){
            for (TeardownDtl item: teardowndto.getDetaillist()){
                item.setCompanyId(teardowndto.getCompanyId());
                //拆卸商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    teardownDtlService.updateById(item);
                else{
                    item.setSourceId(teardowndto.getId());
                    teardownDtlService.save(item);
                }
            }
        }

        // 入库单主表
        inventoryInService.deleteBySourceId(teardowndto.getId());

        if (StringUtils.isNotBlank(teardowndto.getWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setCompanyId(teardowndto.getCompanyId());
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(teardowndto.getWarehouseId());
            inventoryIn.setSourceCode(teardowndto.getCode());
            inventoryIn.setSourceId(teardowndto.getId());
            inventoryIn.setBillType(BillType.INVENTORYIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.TEARDOWN.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        inventoryOutService.deleteBySourceId(teardowndto.getBillType(), teardowndto.getId());
        if (StringUtils.isNotBlank(teardowndto.getWarehouseId())) {
            // 拆卸出库
            InventoryOut inventoryOut = new InventoryOut(teardowndto.getId(), teardowndto.getCode(), BillType.INVENTORYOUT.getId(), BillType.TEARDOWN.getId(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setCompanyId(teardowndto.getCompanyId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return teardowndto.getId();
    }
}
