package org.jeecg.modules.combind.controller;

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
import org.jeecg.modules.combind.dto.TeardownDto;
import org.jeecg.modules.combind.entity.TeardownDtl;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.combind.entity.Teardown;
import org.jeecg.modules.combind.service.TeardownDtlService;
import org.jeecg.modules.combind.service.TeardownService;
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
@RequestMapping("/teardown")
public class TeardownController extends JeecgController<Teardown, TeardownService> {
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private TeardownService teardownService;
    @Autowired
    private TeardownDtlService teardownDtlService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(Teardown teardown,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Teardown> queryWrapper = QueryGenerator.initQueryWrapper(teardown, req.getParameterMap());
        Page<Teardown> page = new Page<>(pageNo, pageSize);
        IPage<Teardown> pageList = teardownService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody TeardownDto teardowndtldto){
        // 采购主表
        String code = billCodeBuilderService.getBillCode(BillType.PURCHASEORDER.getId());
        teardowndtldto.setCode(code);
        teardownService.save(teardowndtldto);

        //采购单子表
        List<InventoryInMtl> detaillist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(teardowndtldto.getDetaillist())){
            List<TeardownDtl> mtls = teardowndtldto.getDetaillist().stream().filter(o->StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //采购商品详情
                o.setCode(code);
                o.setSourceId(teardowndtldto.getId());
            });
            teardownDtlService.saveBatch(mtls);
        }
        if (StringUtils.isNotBlank(teardowndtldto.getWarehouseId())) {

            // 入库单主表
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
        return Result.ok(teardowndtldto.getId());
    }

    @PutMapping("/edit")
    @Transactional
    public Result<?> edit(@RequestBody TeardownDto teardowndtldto){

        // 采购主表
        teardownService.updateById(teardowndtldto);
        if (teardowndtldto.getDetaillist().size() > 0){
            for (TeardownDtl item: teardowndtldto.getDetaillist()){
                //采购商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    teardownDtlService.updateById(item);
                else{
                    item.setSourceId(teardowndtldto.getId());
                    teardownDtlService.save(item);
                }
            }
        }

        // 入库单主表
        inventoryInService.deleteBySourceId(teardowndtldto.getId());

        if (StringUtils.isNotBlank(teardowndtldto.getWarehouseId())) {

            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(teardowndtldto.getWarehouseId());
//            inventoryIn.setPutInTime(teardowndtldto.getPutInTime());
            inventoryIn.setSourceCode(teardowndtldto.getCode());
            inventoryIn.setSourceId(teardowndtldto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.PURCHASEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        return Result.ok(teardowndtldto.getId());
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id){
        teardownService.removeById(id);
        teardownDtlService.deleteBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        teardownService.removeByIds(Arrays.asList(ids.split(",")));
        teardownDtlService.deleteBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id){
        Teardown teardown = teardownService.getById(id);
        System.out.println(teardown.getId());
        if (teardown == null){
            return Result.ok("未找到对应数据");
        }
        TeardownDto teardowndtldto = new TeardownDto();
        teardowndtldto.setId(teardown.getId());
        teardowndtldto.setContent(teardown.getContent());
        teardowndtldto.setWarehouseId(teardown.getWarehouseId());
        teardowndtldto.setCode(teardown.getCode());
        teardowndtldto.setCreateTime(teardown.getCreateTime());
        teardowndtldto.setDetaillist(teardownDtlService.findBySourceId(teardown.getId()));

        return Result.ok(teardowndtldto);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code){

        LambdaQueryWrapper<Teardown> lambdaQueryWrapper = new LambdaQueryWrapper<Teardown>().eq(Teardown::getCode, code);
        Teardown teardown = teardownService.getOne(lambdaQueryWrapper);
        if (teardown == null){
            return Result.ok("未找到对应数据");
        }
        return Result.ok(teardown);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Teardown teardown){
        return super.exportXls(request, teardown,Teardown.class, "采购列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response){
        return super.importExcel(request, response, Teardown.class);
    }
}
