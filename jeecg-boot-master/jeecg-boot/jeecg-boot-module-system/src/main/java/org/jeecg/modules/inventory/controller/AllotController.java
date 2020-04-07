package org.jeecg.modules.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.enums.BillStatus;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.inventory.dto.AllotDto;
import org.jeecg.modules.inventory.entity.Allot;
import org.jeecg.modules.inventory.entity.AllotDtl;
import org.jeecg.modules.inventory.service.AllotDtlService;
import org.jeecg.modules.inventory.service.AllotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "采购列表")
@RestController
@RequestMapping("/allot")
public class AllotController extends JeecgController<Allot, AllotService> {
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private AllotService allotService;
    @Autowired
    private AllotDtlService allotDtlService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(Allot allot,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Allot> queryWrapper = QueryGenerator.initQueryWrapper(allot, req.getParameterMap());
        Page<Allot> page = new Page<>(pageNo, pageSize);
        IPage<Allot> pageList = allotService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody AllotDto allotdto){

        // 采购主表
        String code = billCodeBuilderService.getBillCode(BillType.PURCHASEORDER.getId());
        allotdto.setCode(code);
        allotService.save(allotdto);

        //采购单子表
        List<InventoryInMtl> detaillist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(allotdto.getDetaillist())){
            List<AllotDtl> mtls = allotdto.getDetaillist().stream().filter(o->StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //采购商品详情
                o.setCode(code);
                o.setSourceId(allotdto.getId());
            });
            allotDtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(allotdto.getFromWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(allotdto.getFromWarehouseId());
//            inventoryIn.setPutInTime(allotdto.getPutInTime());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(allotdto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.PURCHASEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        return Result.ok(allotdto.getId());
    }

    @PutMapping("/edit")
    @Transactional
    public Result<?> edit(@RequestBody AllotDto allotdto){

        // 采购主表
        allotService.updateById(allotdto);
        if (allotdto.getDetaillist().size() > 0){
            for (AllotDtl item: allotdto.getDetaillist()){
                //采购商品详情
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

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(allotdto.getFromWarehouseId());
//            inventoryIn.setPutInTime(allotdto.getPutInTime());
            inventoryIn.setSourceCode(allotdto.getCode());
            inventoryIn.setSourceId(allotdto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.PURCHASEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        return Result.ok(allotdto.getId());
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id){
        allotService.removeById(id);
        allotDtlService.deleteBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        allotService.removeByIds(Arrays.asList(ids.split(",")));
        allotDtlService.deleteBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id){
        Allot allot = allotService.getById(id);
        System.out.println(allot.getId());
        if (allot == null){
            return Result.ok("未找到对应数据");
        }
        AllotDto allotdto = new AllotDto();
        allotdto.setId(allot.getId());
        allotdto.setContent(allot.getContent());
        allotdto.setFromWarehouseId(allot.getFromWarehouseId());
        allotdto.setCode(allot.getCode());
        allotdto.setCreateTime(allot.getCreateTime());
        allotdto.setDetaillist(allotDtlService.findBySourceId(allot.getId()));

        return Result.ok(allotdto);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code){

        LambdaQueryWrapper<Allot> lambdaQueryWrapper = new LambdaQueryWrapper<Allot>().eq(Allot::getCode, code);
        Allot allot = allotService.getOne(lambdaQueryWrapper);
        if (allot == null){
            return Result.ok("未找到对应数据");
        }
        return Result.ok(allot);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Allot allot){
        return super.exportXls(request, allot,Allot.class, "采购列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response){
        return super.importExcel(request, response, Allot.class);
    }
}
