package org.jeecg.modules.combind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.combind.dto.TeardownDto;
import org.jeecg.modules.combind.entity.Teardown;
import org.jeecg.modules.combind.mapper.TeardownMapper;
import org.jeecg.modules.combind.service.TeardownService;
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
public class TeardownServiceImpl extends ServiceImpl<TeardownMapper, Teardown> implements TeardownService {

    @Autowired
    private TeardownMapper teardownMapper;
    @Autowired
    private TeardownDtlService teardownDtlService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryOutService inventoryOutService;

    @Override
    @Transactional
    public String saveOrder(TeardownDto teardowndtldto){
        // æ‹†å¸ä¸»è¡¨
        String code = billCodeBuilderService.getBillCode(BillType.PURCHASEORDER.getId());
        teardowndtldto.setCode(code);
        super.save(teardowndtldto);

        //æ‹†å¸å•å­è¡¨
        List<InventoryInMtl> detaillist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(teardowndtldto.getDetaillist())){
            List<TeardownDtl> mtls = teardowndtldto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //æ‹†å¸å•†å“è¯¦æƒ…
                o.setCode(code);
                o.setSourceId(teardowndtldto.getId());
            });
            teardownDtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(teardowndtldto.getWarehouseId())) {

            // å…¥åº“å•ä¸»è¡¨
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(teardowndtldto.getWarehouseId());
//            inventoryIn.setPutInTime(teardowndtldto.getPutInTime());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(teardowndtldto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.PURCHASEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }
    }

    @Override
    @Transactional
    public String editOrder(TeardownDto teardowndtldto){
        // æ‹†å¸ä¸»è¡¨
        super.updateById(teardowndtldto);
        if (teardowndtldto.getDetaillist().size() > 0){
            for (TeardownDtl item: teardowndtldto.getDetaillist()){
                //æ‹†å¸å•†å“è¯¦æƒ…
                if (item.getId() != null && item.getId().length() > 0)
                    teardownDtlService.updateById(item);
                else{
                    item.setSourceId(teardowndtldto.getId());
                    teardownDtlService.save(item);
                }
            }
        }

        // å…¥åº“å•ä¸»è¡¨
        inventoryInService.deleteBySourceId(teardowndtldto.getId());

        if (StringUtils.isNotBlank(teardowndtldto.getWarehouseId())) {

            // å…¥åº“å•ä¸»è¡¨
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(teardowndtldto.getWarehouseId());
            inventoryIn.setPutInTime(new Date());
            inventoryIn.setSourceCode(teardowndtldto.getCode());
            inventoryIn.setSourceId(teardowndtldto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.PURCHASEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        inventoryOutService.deleteBySourceId(allotdto.getId());
        if (StringUtils.isNotBlank(allotdto.getFromWarehouseId())) {
            // é”€å”®å‡ºåº“
            InventoryOut inventoryOut = new InventoryOut(allotdto.getId(), allotdto.getCode(), BillType.STOREOUT.getId(), BillTlÄ&5ÎæÆAËö‹Û‹#è$ÜU×µ2«Êà¿#vqbc¯CO‚G¯iŞöÖ38c†Æá¥ÇE|	ñ·;6‡2±ÂÌÄ*A?5[æŒq–¼¢ªÚHó›»]©òãL±®ºë>ôˆN§`såå³ç’Ğ¬@Şâ"›ƒoWÇMá¹ş¡Ÿøcç¢SØ±ğ8¦‡æ˜Úı¿ß¥BL:…;jœ½İ*WzÇˆ"€?¢eÎK˜¨‹äev‘µxÜƒHİ–œåmU~í±tÖ›ö—¢¨nü!Êò	¶åËªlZÿ/%ë-«©§h5ıób%CµÚëÊ\xQ(‹å¤