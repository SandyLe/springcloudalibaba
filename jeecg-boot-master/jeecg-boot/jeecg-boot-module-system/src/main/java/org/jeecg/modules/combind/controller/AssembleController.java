package org.jeecg.modules.combind.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.entity.MaterialUnit;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.basic.service.MaterialUnitService;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.combind.dto.AssembleDto;
import org.jeecg.modules.combind.entity.Assemble;
import org.jeecg.modules.combind.entity.AssembleDtl;
import org.jeecg.modules.combind.service.AssembleDtlService;
import org.jeecg.modules.combind.service.AssembleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "组装单列表")
@RestController
@RequestMapping("/assemble")
public class AssembleController extends JeecgController<Assemble, AssembleService> {
    @Autowired
    private AssembleService assembleService;
    @Autowired
    private AssembleDtlService assembleDtlService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialUnitService materialUnitService;

    @GetMapping("/getPage")
    public Result<?> queryPageList(Assemble assemble,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Assemble> queryWrapper = QueryGenerator.initQueryWrapper(assemble, req.getParameterMap());
        Page<Assemble> page = new Page<>(pageNo, pageSize);
        IPage<Assemble> pageList = assembleService.page(page, queryWrapper);
        List<Assemble> datas = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(datas)) {
            Map<String, String> warehouseMap = new HashMap<>();
            Map<String, String> mtlMap = new HashMap<>();
            Map<String, String> unitMap = new HashMap<>();

            List<String> warehouseIds = datas.stream().map(Assemble::getWarehouseId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(warehouseIds)) {
                Collection<Warehouse> warehouses = warehouseService.listByIds(warehouseIds);
                warehouseMap.putAll(warehouses.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName)));
            }
            List<String> mtlIds = datas.stream().map(Assemble::getMtlId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(mtlIds)) {
                Collection<Material> materials = materialService.listByIds(mtlIds);
                mtlMap.putAll(materials.stream().collect(Collectors.toMap(Material::getId, Material::getName)));
            }
            List<String> unitIds = datas.stream().map(Assemble::getUnitId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(unitIds)) {
                Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
                unitMap.putAll(units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName)));
            }
            datas.stream().forEach(o -> {
                o.setWarehouse(warehouseMap.get(o.getWarehouseId()));
                o.setMaterial(mtlMap.get(o.getMtlId()));
                o.setUnit(unitMap.get(o.getUnitId()));
            });
        }
        pageList.setRecords(datas);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody AssembleDto assembledto) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(assembledto.getCompanyId())) {
            assembledto.setCompanyId(sysUser.getCompanyId());
        }
        assembleService.saveOrder(assembledto);
        return Result.ok();
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody AssembleDto assembledto) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(assembledto.getCompanyId())) {
            assembledto.setCompanyId(sysUser.getCompanyId());
        }
        assembleService.editOrder(assembledto);
        return Result.ok();
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        assembleService.removeById(id);
        assembleDtlService.deleteBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        assembleService.removeByIds(Arrays.asList(ids.split(",")));
        assembleDtlService.deleteBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/getOne")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Assemble assemble = assembleService.getById(id);
        if (assemble == null) {
            return Result.ok("未找到对应数据");
        }
        AssembleDto assembledtldto = new AssembleDto();
        BeanUtils.copyProperties(assemble, assembledtldto);
        assembledtldto.setDetaillist(assembleDtlService.findBySourceId(assemble.getId()));

        return Result.ok(assembledtldto);
    }

    @GetMapping("/getOneByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code) {

        LambdaQueryWrapper<Assemble> lambdaQueryWrapper = new LambdaQueryWrapper<Assemble>().eq(Assemble::getCode, code);
        Assemble assemble = assembleService.getOne(lambdaQueryWrapper);
        if (assemble == null) {
            return Result.ok("未找到对应数据");
        }
        return Result.ok(assemble);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Assemble assemble) {
        return super.exportXls(request, assemble, Assemble.class, "拆卸单列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Assemble.class);
    }
}
