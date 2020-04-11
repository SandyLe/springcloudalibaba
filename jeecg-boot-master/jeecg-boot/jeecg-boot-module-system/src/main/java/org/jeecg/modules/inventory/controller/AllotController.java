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
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.inventory.entity.*;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.inventory.dto.AllotDto;
import org.jeecg.modules.inventory.service.AllotDtlService;
import org.jeecg.modules.inventory.service.AllotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "调拨列表")
@RestController
@RequestMapping("/allot")
public class AllotController extends JeecgController<Allot, AllotService> {
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private AllotService allotService;
    @Autowired
    private AllotDtlService allotDtlService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(Allot allot,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Allot> queryWrapper = QueryGenerator.initQueryWrapper(allot, req.getParameterMap());
        Page<Allot> page = new Page<>(pageNo, pageSize);
        IPage<Allot> pageList = allotService.page(page, queryWrapper);
        List<Allot> datas = pageList.getRecords();
        List<String> fromWarehoseIds = datas.stream().map(Allot::getFromWarehouseId).collect(Collectors.toList());
        List<String> toWarehouseIds = datas.stream().map(Allot::getToWarehouseId).collect(Collectors.toList());
        fromWarehoseIds.addAll(toWarehouseIds);
        List<String> warehouseIds = fromWarehoseIds.stream().distinct().collect(Collectors.toList());
        Collection<Warehouse> warehouses =warehouseService.listByIds(warehouseIds);
        Map<String, Warehouse> warehouseMap = warehouses.stream().collect(Collectors.toMap(Warehouse::getId, o->o));
        datas.stream().forEach(o->{
            o.setFromWarehouse(warehouseMap.get(o.getFromWarehouseId()).getName());
            o.setToWarehouse(warehouseMap.get(o.getToWarehouseId()).getName());
        });
        pageList.setRecords(datas);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody AllotDto allotdto){

        String resultId = allotService.saveOrder(allotdto);
        Result<Object> result = Result.ok();
        result.setResult(resultId);
        return result;
    }

    @PutMapping("/edit")
    @Transactional
    public Result<?> edit(@RequestBody AllotDto allotdto){

        allotService.editOrder(allotdto);

        return Result.ok();
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
        return super.exportXls(request, allot,Allot.class, "调拨列�l&��@�`6
�������;���g�� (n49���\(w��d�"��L6vb�ɮoh��9:$B�Gks���8z��JX[÷���7��ƕ�Q�rڐf��5Y���< �p�.xpfIc��y=�8��Ac�u��\+�߈^�s?̤����N- *�0��ek���{�I:u��
/��tufC�?��,h�-0l����kE���