package org.jeecg.modules.basic.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.dto.MaterialExcelDto;
import org.jeecg.modules.basic.dto.MaterialPriceExcelDto;
import org.jeecg.modules.basic.dto.SaleOrderMtlPriceDto;
import org.jeecg.modules.basic.entity.*;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.service.*;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "产品价格")
@RestController
@RequestMapping("/materialPrice")
public class MaterialPriceController {

    @Autowired
    private MaterialPriceService materialPriceService;
    @Autowired
    private MaterialUnitService materialUnitService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private CustomerTypeService customerTypeService;
    @Autowired
    private CustomerService customerService;



    /**
     * 分页列表查询
     *
     * @param materialPrice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品价格数据列表", notes = "获取所有产品价格数据列表")
    @GetMapping(value = "/getPage")
    public Result<?> list(MaterialPrice materialPrice, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<MaterialPrice> queryWrapper = QueryGenerator.initQueryWrapper(materialPrice, req.getParameterMap());
        Page<MaterialPrice> page = new Page<MaterialPrice>(pageNo, pageSize);
        IPage<MaterialPrice> pageList = materialPriceService.page(page, queryWrapper);
        List<MaterialPrice> records = pageList.getRecords();
        List<String> mtlIds = records.stream().map(MaterialPrice::getMaterialId).collect(Collectors.toList());
        List<String> typeIds = records.stream().map(MaterialPrice::getCustomerTypeId).collect(Collectors.toList());
        List<String> unitIds = records.stream().map(MaterialPrice::getUnitId).collect(Collectors.toList());
        Collection<Material> materials = materialService.listByIds(mtlIds);
        Collection<CustomerType> types = customerTypeService.listByIds(typeIds);
        Collection<MaterialUnit> units = materialUnitService.listByIds(unitIds);
        Map<String, String> mtlMap = materials.stream().collect(Collectors.toMap(Material::getId, Material::getName));
        Map<String, String> typeMap = types.stream().collect(Collectors.toMap(CustomerType::getId, CustomerType::getName));
        Map<String, String> unitMap = units.stream().collect(Collectors.toMap(MaterialUnit::getId, MaterialUnit::getName));
        records.stream().forEach(o->{
            o.setCustomerType(typeMap.get(o.getCustomerTypeId()));
            o.setMaterial(mtlMap.get(o.getMaterialId()));
            o.setUnit(unitMap.get(o.getUnitId()));
        });
        pageList.setRecords(records);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 获取所有数据
     *
     * @param materialPrice
     * @param req
     * @return
     */
    @ApiOperation(value = "获取产品价格数据", notes = "获取所有产品价格数据")
    @GetMapping(value = "/getList")
    public Result<?> getList(MaterialPrice materialPrice, HttpServletRequest req) {
        QueryWrapper<MaterialPrice> queryWrapper = QueryGenerator.initQueryWrapper(materialPrice, req.getParameterMap());
        List<MaterialPrice> list = materialPriceService.list(queryWrapper);
        return Result.ok(list);
    }
    /**
     * 添加
     *
     * @param materialPrice
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加产品价格")
    @ApiOperation(value = "添加产品价格", notes = "添加产品价格")
    public Result<?> add(@RequestBody MaterialPrice materialPrice) {
        materialPriceService.save(materialPrice);
        return Result.ok("添加成功！");
    }

    /**
     * 修改
     *
     * @param materialPrice
     * @return
     */
    @PostMapping(value = "/edit")
    @AutoLog(value = "修改产品价格")
    @ApiOperation(value = "修改产品价格", notes = "修改产品价格")
    public Result<?> edit(@RequestBody MaterialPrice materialPrice){
        materialPriceService.updateById(materialPrice);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除产品价格")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除产品价格", notes = "通过ID删除产品价格")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        materialPriceService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除产品价格", notes = "批量删除产品价格")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.materialPriceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过ID查询产品价格", notes = "通过ID查询产品价格")
    public Result<?> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        MaterialPrice materialPrice = materialPriceService.getById(id);
        return Result.ok(materialPrice);
    }

    /**
     * 获取价格
     *
     * @param
     * @return
     */
    @GetMapping(value = "/getMtlPrice")
    @ApiOperation(value = "通过ID查询产品价格", notes = "通过ID查询产品价格")
    public Result<?> getMtlPrice(@ApiParam(name = "customerId", value = "客户id", required = true) @RequestParam(name = "customerId", required = true) String customerId,
                                 @ApiParam(name = "mtlId", value = "产品id", required = true) @RequestParam(name = "mtlId", required = true) String mtlId,
                                 @ApiParam(name = "unitId", value = "单位id", required = true) @RequestParam(name = "unitId", required = false) String unitId) {
        SaleOrderMtlPriceDto dto = new SaleOrderMtlPriceDto();
        Material material = materialService.getById(mtlId);
        dto.setMtlId(mtlId);
        dto.setMtlCode(material.getCode());
        dto.setMtlName(material.getName());
        dto.setSpecification(material.getSpecification());
        dto.setUnitId(StringUtils.isNotBlank(unitId) ? unitId : material.getUnitId());

        Customer customer = customerService.getById(customerId);
        MaterialPrice materialPrice = materialPriceService.getMtlPrice(customer.getCustomerTypeId(), mtlId, dto.getUnitId());
        dto.setPrice(null != materialPrice ? materialPrice.getPrice() : null);
        return Result.ok(dto);
    }


    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
//    @RequiresPermissions("user:import")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @Transactional
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        Collection<CustomerType> customerTypes = customerTypeService.list();
        Collection<Material> materials = materialService.list();
        Collection<MaterialUnit> materialUnits = materialUnitService.list();

        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<MaterialPriceExcelDto> materialList = ExcelImportUtil.importExcel(file.getInputStream(), MaterialPriceExcelDto.class, params);
                if (CollectionUtils.isNotEmpty(materialList)){

                    if (materialList.stream().anyMatch(p->StringUtils.isEmpty(p.getName()))){
                        return Result.error(String.format("抱歉！“产品名称”列不能为空"));
                    }
                    Collection<String> brandsSubtract  = CollectionUtils.removeAll(materialList.stream().map(p->p.getName()).collect(Collectors.toList()),
                            materials.stream().map(p->p.getName()).collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(brandsSubtract)){
                        return Result.error(String.format("抱歉！请先新增以下产品: %s", StringUtils.join(brandsSubtract.toArray(),",")));
                    }


                    if (materialList.stream().anyMatch(p->StringUtils.isEmpty(p.getCustomerType()))){
                        return Result.error(String.format("抱歉！“客户类型”列不能为空"));
                    }
                    Collection<String> typesSubtract = CollectionUtils.removeAll(materialList.stream().map(p->p.getCustomerType()).collect(Collectors.toList()),
                            customerTypes.stream().map(p->p.getName()).collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(typesSubtract)){
                        return Result.error(String.format("抱歉！请先新增以下客户类型: %s", StringUtils.join(typesSubtract.toArray(),",")));
                    }


                    if (materialList.stream().anyMatch(p->StringUtils.isEmpty(p.getUnit()))){
                        return Result.error(String.format("抱歉！“计量单位”列不能为空"));
                    }
                    Collection<String> unitsSubtract = CollectionUtils.removeAll(materialList.stream().map(p->p.getUnit()).collect(Collectors.toList()),
                            materialUnits.stream().map(p->p.getName()).collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(typesSubtract)){
                        return Result.error(String.format("抱歉！请先新增以下计量单位: %s", StringUtils.join(unitsSubtract.toArray(),",")));
                    }


                    List<MaterialPrice> excelMaterial = new ArrayList<>();
                    MaterialPrice materialPrice;
                    for (MaterialPriceExcelDto materialExcelDto : materialList) {
//                        System.out.println(JSON.toJSONString(materialExcelDto));
                        materialPrice = new MaterialPrice();
                        materialPrice.setPrice(new BigDecimal(materialExcelDto.getPrice()));
                        materialPrice.setCustomerTypeId(customerTypes.stream().filter(p-> ObjectUtils.equals(p.getName(), materialExcelDto.getCustomerType()) ).findFirst().get().getId());
                        materialPrice.setUnitId(materialUnits.stream().filter(p-> ObjectUtils.equals(p.getName(), materialExcelDto.getUnit()) ).findFirst().get().getId());
                        materialPrice.setMaterialId(materials.stream().filter(p-> ObjectUtils.equals(p.getName(), materialExcelDto.getName()) ).findFirst().get().getId());
                        excelMaterial.add(materialPrice);
                    }
//                    System.out.println(JSON.toJSONString(excelMaterial));
                    materialPriceService.saveBatch(excelMaterial);
                    return Result.ok("文件导入成功！数据行数：" + materialList.size());
                }
                return Result.error("抱歉! Excel中没有任何数据");

            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage(),e);
                return Result.error("抱歉! 发生错误:"+ e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return Result.error("文件导入失败！");
    }
}
