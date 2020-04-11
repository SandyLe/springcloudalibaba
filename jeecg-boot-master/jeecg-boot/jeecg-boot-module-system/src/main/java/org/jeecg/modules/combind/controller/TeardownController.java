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
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
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
@Api(tags = "拆卸列表")
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
    public Result<?> add(@RequestBody TeardownDto teardowndto){
        teardownService.saveOrder(teardowndto);
        return Result.ok(teardowndto.getId());
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody TeardownDto teardowndtldto){


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
            return Result.ok("未找到对应数据");l�*���º��iŭ�ϧ�U0 u��GN>������5A*�:d��� �]׺2>��d4.�����P8���1���%u ���p����C�?�di��Y��^b�����=`����"�� A�F�&.�#�B�Y�|x����`�8�$c���Bn��L�B�za�y�Onn��l�_P-mS�V�L�Gh�q=���\��g�->0�����
��G�a�����6h~jI-���>w+��}g�0a&�
ܼ67�:m��r>>*���7�'��t!63�j�YtW�ڽ{I���+�rFuO21Qs?��,Y��.�P�|�~���%��D]�7�k�Ch��xM����jt�I��e�i���+6/�($�f��:{7ϋ�D<��{q� 3�x_��d Yp��@=Z�Nh�qqc�����̩�u)z3�5*���(���o$�����1=��!�&���������dW����