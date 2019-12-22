package org.jeecg.modules.purchases.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.purchases.entity.Purchases;
import org.jeecg.modules.purchases.service.PurchasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@Api(tags = "采购接口")
@RestController
@RequestMapping("/purchases")
public class PurchasesController extends JeecgController<Purchases, PurchasesService> {
    @Autowired
    private PurchasesService purchasesService;

    /**
     * 分页列表查询
     *
     * @param purchases
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Purchases purchases,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Purchases> queryWrapper = QueryGenerator.initQueryWrapper(purchases, req.getParameterMap());
        Page<Purchases> page = new Page<Purchases>(pageNo, pageSize);
        IPage<Purchases> pageList = purchasesService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     *   添加
     *
     * @param purchases
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Purchases purchases) {
        purchasesService.save(purchases);
        return Result.ok("添加成功！");
    }

    /**
     *  编辑
     *
     * @param purchases
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Purchases purchases) {
        purchasesService.updateById(purchases);
        return Result.ok("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        purchasesService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.purchasesService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        Purchases purchases = purchasesService.getById(id);
        if(purchases==null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(purchases);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param purchases
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Purchases purchases) {
        return super.exportXls(request, purchases, Purchases.class, "采购列表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Purchases.class);
    }

}
