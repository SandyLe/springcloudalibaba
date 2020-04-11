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
        // ÁªÑË£Ö‰∏ªË°®
        String code = billCodeBuilderService.getBillCode(BillType.ASSEMBLE.getId());
        assembledto.setCode(code);
        super.save(assembledto);

        //ÁªÑË£ÖÂçïÂ≠êË°®
        if (CollectionUtils.isNotEmpty(assembledto.getDetaillist())){
            List<AssembleDtl> mtls = assembledto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //ÁªÑË£ÖÂïÜÂìÅËØ¶ÊÉÖ
                o.setCode(code);
                o.setSourceId(assembledto.getId());
            });
            assembleDtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(assembledto.getWarehouseId())) {

            // ÂÖ•Â∫ìÂçï‰∏ªË°®
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
            // ÈîÄÂîÆÂá∫Â∫ì
            InventoryOut inventoryOut = new InventoryOut(allotdto.getId(), allotdto.getCode(), BillType.STOREOUT.getId(), BillType.ALLOT.getId(), allotdto.getFromWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
    }

    @Override
    @Transactional
    public String editOrder(AssembleDto assembledto){

        // ÁªÑË£Ö‰∏ªË°®
        assembleService.updateById(assembledto);
        if (assembledto.getDetaillist().size() > 0){
            for (AssembleDtl item: assembledto.getDetaillist()){
                //ÁªÑË£ÖÂïÜÂìÅËØ¶ÊÉÖ
                if (item.getId() != null && item.getId().length() > 0)
                    assembleDtlService.updateById(item);
                else{
                    item.setSourceId(assembledto.getId());
                    assembleDtlService.save(item);
                }
            }
        }

        // ÂÖ•Â∫ìÂçï‰∏ªË°®
        inventoryInService.deleteBySourceId(assembledto.getId());

        if (StringUtils.isNotBlank(assembledto.getWarehouseId())) {

            // ÂÖ•Â∫ìÂçï‰∏ªË°®
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
            // ÈîÄÂîÆÂá∫Â∫ì
            InventoryOut inventoryOut = new InventoryOut(assembledto.getId(), assembledto.getCode(), BillType.STOREOUT.getId(), BillType.ASSEMBLE.getId(), assembledto.getFromWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            invlHQˆ|ƒòÄd‡q˜≠†⁄≤\lhÕ“n∞UcÎï5OìCπSÖƒ<®ÉOÑ€™´´Hl√Ûjﬂ°ˇ5‡”D˜Œ¨/ÀDrôÅ