package org.jeecg.modules.purchase.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.purchase.dto.Purchasedtldto;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.Purchasedtl;
import org.jeecg.modules.purchase.entity.Purchasereturn;
import org.jeecg.modules.purchase.service.IPurchaseService;
import org.jeecg.modules.purchase.service.IPurchasedtlService;
import org.jeecg.modules.purchase.service.IPurchasereturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@Api(tags = "采购退货")
@RestController
@RequestMapping("/purchasereturn")
public class PurchasereturnController extends JeecgController<Purchasereturn, IPurchasereturnService> {

    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Autowired
    private IPurchasereturnService purchasereturnService;

    @GetMapping("/getPaged")
    public Result<?> queryPageList(Purchasereturn purchasereturn,
                                   @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req)
    {
        QueryWrapper<Purchasereturn> queryWrapper = QueryGenerator.initQueryWrapper(purchasereturn, req.getParameterMap());
        Page<Purchasereturn> page = new Page<>(pageNo, pageSize);
        IPage<Purchasereturn> pageList = purchasereturnService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody Purchasereturn purchasereturn){

        purchasereturn.setCode(billCodeBuilderService.getBillCode(BillType.PURCHASERETURNORDER.getId()));
        purchasereturnService.save(purchasereturn);
//        if (purchasedtldto.getDetaillist().size() > 0){
//            for (Purchasedtl item: purchasedtldto.getDetaillist()){
//                item.setSourceId(purchasedtldto.getId());
//                purchasedtlService.save(item);
//            }
//        }
        return Result.ok("添加成功");
    }

    @PutMapping("/edit")
    public Result<?> edit(@RequestBody Purchasereturn purchasereturn){
        purchasereturnService.updateById(purchasereturn);
//        if (purchasedtldto.getDetaillist().size() > 0){
//            for (Purchasedtl item: purchasedtldto.getDetaillist()){
//                if (item.getId() != null && item.getId().length() > 0)
//                    purchasedtlService.updateById(item);
//                else{
//                    item.setSourceId(purchasedtldto.getId());
//                    purchasedtlService.save(item);
//                }
//            }
//        }
        return Result.ok("编辑成功");
    }

    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id){
        purchasereturnService.removeById(id);
//        purchaseService.removeById(id);
//        purchasedtlService.removeBySourceId(id);
        return Result.ok("删除成功!");
    }

    @DeleteMapping("/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        purchasereturnService.removeByIds(Arrays.asList(ids.split(",")));
//        purchaseService.removeByIds(Arrays.asList(ids.split(",")));
//        purchasedtlService.removeBySourceIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id){
        Purchasereturn purchasereturn = purchasereturnService.getById(id);
//        Purchase purchase = purchaseService.getById(id);
//        System.out.println(purchase.getId());
        if (purchasereturn == null){
            return Result.ok("未找到对应数据");
        }
//        Purchasedtldto purchasedtldto = new Purchasedtldto();
//        purchasedtldto.setId(purchase.getId());
//        purchasedtldto.setVendorId(purchase.getVendorId());
//        purchasedtldto.setContent(purchase.getContent());
//        purchasedtldto.setWarehouseId(purchase.getWarehouseId());
//        purchasedtldto.setAccount(purchase.getAccount());
//        purchasedtldto.setPayamount(purchase.getPayamount());
//        purchasedtldto.setBilldate(purchase.getBilldate());
//        purchasedtldto.setTotalamount(purchase.getTotalamount());
//        purchasedtldto.setCreateTime(purchase.getCreateTime());
//        purchasedtldto.setDetaillist(purchasedtlService.queryBySourceId(purchase.getId()));
        return Result.ok(purchasereturn);
    }

    @RequestMapping("/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Purchasereturn purchasereturn){
        return super.exportXls(request, purchasereturn,Purchasereturn.class, "采购列表");
    }

    @PostMapping("importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response){
        return super.importExcel(request, response, Purchasereturn.class);
    }













}
