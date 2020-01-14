package org.jeecg.modules.purchase.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.purchase.dto.Purchasedtldto;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.Purchasedtl;
import org.jeecg.modules.purchase.service.IPurchaseService;
import org.jeecg.modules.purchase.service.IPurchasedtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "采购列表")
@RestController
@RequestMapping("/purchase")
public class PurchaseController extends JeecgController<Purchase, IPurchaseService> {

    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private IPurchasedtlService purchasedtlService;

    @GetMapping("/getPaged")
    public Result<?> queryPageList(Purchase purchase,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req)
    {
        QueryWrapper<Purchase> queryWrapper = QueryGenerator.initQueryWrapper(purchase, req.getParameterMap());
        Page<Purchase> page = new Page<>(pageNo, pageSize);
        IPage<Purchase> pageList = purchaseService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody Purchasedtldto purchasedtldto){
        purchaseService.save(purchasedtldto);
        if (purchasedtldto.getDetaillist().size() > 0){
            for (Purchasedtl item: purchasedtldto.getDetaillist()){
                item.setSourceId(purchasedtldto.getId());
                purchasedtlService.save(item);
            }
        }
        return Result.ok("添加成功");
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody Purchasedtldto purchasedtldto){
        purchaseService.updateById(purchasedtldto);
        if (purchasedtldto.getDetaillist().size() > 0){
            for (Purchasedtl item: purchasedtldto.getDetaillist()){
                if (item.getId() != null && item.getId().length() > 0)
                    purchasedtlService.updateById(item);
                else{
                    item.setSourceId(purchasedtldto.getId());
                    purchasedtlService.save(item);
                }
            }
        }
        return Result.ok("编辑成功");
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id){
        purchaseService.removeById(id);
        purchasedtlService.removeBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        purchaseService.removeByIds(Arrays.asList(ids.split(",")));
        purchasedtlService.removeBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id){
        Purchase purchase = purchaseService.getById(id);
        System.out.println(purchase.getId());
        if (purchase == null){
            return Result.ok("未找到对应数据");
        }
        Purchasedtldto purchasedtldto = new Purchasedtldto();
        purchasedtldto.setId(purchase.getId());
        purchasedtldto.setVendorId(purchase.getVendorId());
        purchasedtldto.setDescription(purchase.getDescription());
        purchasedtldto.setWarehouseId(purchase.getWarehouseId());
        purchasedtldto.setAccount(purchase.getAccount());
        purchasedtldto.setPayamount(purchase.getPayamount());
        purchasedtldto.setBilldate(purchase.getBilldate());
        purchasedtldto.setTotalamount(purchase.getTotalamount());
        purchasedtldto.setCreateTime(purchase.getCreateTime());
        purchasedtldto.setDetaillist(purchasedtlService.queryBySourceId(purchase.getId()));
        return Result.ok(purchasedtldto);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Purchase purchase){
        return super.exportXls(request, purchase,Purchase.class, "采购列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response){
        return super.importExcel(request, response, Purchase.class);
    }













}
