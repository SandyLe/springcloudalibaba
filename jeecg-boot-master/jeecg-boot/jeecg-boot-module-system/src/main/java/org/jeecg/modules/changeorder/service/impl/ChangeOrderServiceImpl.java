package org.jeecg.modules.changeorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.changeorder.dto.ChangeOrderDto;
import org.jeecg.modules.changeorder.entity.ChangeOrder;
import org.jeecg.modules.changeorder.entity.ChangeOrderDtl;
import org.jeecg.modules.changeorder.mapper.ChangeOrderMapper;
import org.jeecg.modules.changeorder.service.ChangeOrderDtlService;
import org.jeecg.modules.changeorder.service.ChangeOrderService;
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
public class ChangeOrderServiceImpl extends ServiceImpl<ChangeOrderMapper, ChangeOrder> implements ChangeOrderService {

    @Autowired
    private ChangeOrderDtlService changeOrderDtlService;
    @Lazy
    @Autowired
    private InventoryOutService inventoryOutService;
    @Lazy
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    @Transactional
    public String saveOrder(ChangeOrderDto changeOrderdto){
        // 换货主表
        String code = billCodeBuilderService.getBillCode(BillType.CHANGEORDER.getId());
        changeOrderdto.setCode(code);
        changeOrderdto.setBillStatus(BillStatus.NEW.getId());
        super.save(changeOrderdto);

        //换货单子表
        if (CollectionUtils.isNotEmpty(changeOrderdto.getDetaillist())){
            List<ChangeOrderDtl> mtls = changeOrderdto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //组装商品详情
                o.setCode(code);
                o.setCompanyId(changeOrderdto.getCompanyId());
                o.setSourceId(changeOrderdto.getId());
            });
            changeOrderDtlService.saveBatch(mtls);
        }

        // 换货出库通知
        InventoryOut inventoryOut = new InventoryOut(changeOrderdto.getId(), changeOrderdto.getCode(), BillType.INVENTORYOUT.getId(), BillType.CHANGEORDER.getId(), BillStatus.TOSTOCKOUT.getId());
        inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
        inventoryOut.setCompanyId(changeOrderdto.getCompanyId());
        inventoryOutService.saveToInventoryOut(inventoryOut);
        // 换货入库通知
        InventoryIn inventoryIn = new InventoryIn();
        inventoryIn.setCompanyId(changeOrderdto.getCompanyId());
        inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
        inventoryIn.setSourceCode(code);
        inventoryIn.setSourceId(changeOrderdto.getId());
        inventoryIn.setBillType(BillType.INVENTORYIN.getId());
        inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
        inventoryIn.setSourceBillType(BillType.CHANGEORDER.getId());
        inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
        inventoryInService.saveToInventoryIn(inventoryIn);

        return changeOrderdto.getId();
    }

    @Override
    @Transactional
    public String editOrder(ChangeOrderDto changeOrderdto){

        // 组装主表
        super.updateById(changeOrderdto);
        if (changeOrderdto.getDetaillist().size() > 0){
            for (ChangeOrderDtl item: changeOrderdto.getDetaillist()){
                if (StringUtils.isBlank(item.getCompanyId())) {
                    item.setCompanyId(changeOrderdto.getCompanyId());
                }
                //组装商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    changeOrderDtlService.updateById(item);
                else{
                    item.setSourceId(changeOrderdto.getId());
                    changeOrderDtlService.save(item);
                }
            }
        }

        // 配件出库
        inventoryOutService.deleteBySourceId(changeOrderdto.getBillType(), changeOrderdto.getId());
        InventoryOut inventoryOut = new InventoryOut(changeOrderdto.getId(), changeOrderdto.getCode(), BillType.INVENTORYOUT.getId(), BillType.CHANGEORDER.getId(), BillStatus.TOSTOCKOUT.getId());
        inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
        inventoryOut.setCompanyId(changeOrderdto.getCompanyId());
        inventoryOutService.saveToInventoryOut(inventoryOut);


        inventoryInService.deleteBySourceId(changeOrderdto.getId());
        InventoryIn inventoryIn = new InventoryIn();
        inventoryIn.setCompanyId(changeOrderdto.getCompanyId());
        inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
        inventoryIn.setSourceCode(changeOrderdto.getCode());
        inventoryIn.setSourceId(changeOrderdto.getId());
        inventoryIn.setBillType(BillType.INVENTORYIN.getId());
        inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
        inventoryIn.setSourceBillType(BillType.CHANGEORDER.getId());
        inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
        inventoryInService.saveToInventoryIn(inventoryIn);
        return changeOrderdto.getId();
    }
}
