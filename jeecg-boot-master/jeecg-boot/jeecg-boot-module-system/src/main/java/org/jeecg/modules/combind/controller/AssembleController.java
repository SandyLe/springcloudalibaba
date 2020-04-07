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
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.combind.dto.AssembleDto;
import org.jeecg.modules.combind.dto.AssembleInventoryDto;
import org.jeecg.modules.combind.entity.Assemble;
import org.jeecg.modules.combind.entity.AssembleDtl;
import org.jeecg.modules.combind.service.AssembleDtlService;
import org.jeecg.modules.combind.service.AssembleService;
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
@Api(tags = "组装单列表")
@RestController
@RequestMapping("/assemble")
public class AssembleController extends JeecgController<Assemble, AssembleService> {
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private AssembleService assembleService;
    @Autowired
    private AssembleDtlService assembleDtlService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(Assemble Assemble,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Assemble> queryWrapper = QueryGenerator.initQueryWrapper(Assemble, req.getParameterMap());
        Page<Assemble> page = new Page<>(pageNo, pageSize);
        IPage<Assemble> pageList = assembleService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody AssembleDto assembledto){
        AssembleDto dto = new AssembleDto();
        dto.setMsg("添加失败");

        // 采购主表
        String code = billCodeBuilderService.getBillCode(BillType.AssembleORDER.getId());
        assembledto.setCode(code);
        assembleService.save(assembledto);

        //采购单子表
        List<InventoryInMtl> detaillist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(assembledto.getDetaillist())){
            List<AssembleMtl> mtls = assembledto.getDetaillist().stream().filter(o->StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //采购商品详情
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
            inventoryIn.setPutInTime(assembledto.getPutInTime());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(assembledto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.AssembleORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }

        dto.setMsg("添加成功");
        return Result.ok(dto);
    }

    @PutMapping("/edit")
    @Transactional
    public Result<?> edit(@RequestBody AssembleDto assembledto){
        AssembleDto dto = new AssembleDto();
        dto.setMsg("编辑失败");

        // 采购主表
        assembleService.updateById(assembledto);
        if (assembledto.getDetaillist().size() > 0){
            for (AssembleDtl item: assembledto.getDetaillist()){
                //采购商品详情
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
            inventoryIn.setPutInTime(assembledto.getPutInTime());
            inventoryIn.setSourceCode(assembledto.getCode());
            inventoryIn.setSourceId(assembledto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.AssembleORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
            dto.setInventory(inventoryIn);
        }
        dto.setMsg("编辑成功");

        return Result.ok(dto);
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id){
        assembleService.removeById(id);
        assembleDtlService.removeBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        assembleService.removeByIds(Arrays.asList(ids.split(",")));
        assembleDtlService.removeBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id){
        Assemble assemble = assembleService.getById(id);
        if (assemble == null){
            return Result.ok("未找到对应数据");
        }
        AssembleDto assembledtldto = new AssembleDto();
        assembledtldto.setId(assemble.getId());
        assembledtldto.setVendorId(assemble.getVendorId());
        assembledtldto.setContent(assemble.getContent());
        assembledtldto.setWarehouseId(assemble.getWarehouseId());
        assembledtldto.setAccount(assemble.getAccount());
        assembledtldto.setPayamount(assemble.getPayamount());
        assembledtldto.setBilldate(assemble.getBilldate());
        assembledtldto.setTotalamount(assemble.getTotalamount());
        assembledtldto.setCode(assemble.getCode());
        assembledtldto.setCreateTime(assemble.getCreateTime());
        assembledtldto.setBatchNo(assemble.getBatchNo());
        assembledtldto.setDetaillist(AssembleMtlService.queryBySourceId(assemble.getId()));

        return Result.ok(assembledtldto);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code){

        LambdaQueryWrapper<Assemble> lambdaQueryWrapper = new LambdaQueryWrapper<Assemble>().eq(Assemble::getCode, code);
        Assemble assemble = assembleService.getOne(lambdaQueryWrapper);
        if (assemble == null){
            return Result.ok("未找到对应数据");
        }
        return Result.ok(assemble);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Assemble Assemble){
        return super.exportXls(request, Assemble,Assemble.class, "采购列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response){
        return super.importExcel(request, response, Assemble.class);
    }
}
