package org.jeecg.modules.basic.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.basic.enums.DiscountType;
import org.jeecg.modules.basic.enums.EnumConvertUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "基础通用接口")
@RestController
@RequestMapping("/basic")
public class BasicController {

    @ApiOperation(value = "获取枚举列表", notes = "获取枚举列表")
    @GetMapping(value = {"/fc/enum/{entity}/getList"})
    public Result<?> getList(@PathVariable("entity") String entity,
                                @RequestParam(name = "ids", required = false) @ApiParam(value = "包含或者排除的id") String[] ids,
                                @RequestParam(name = "include", required = false, defaultValue = "true") @ApiParam(value = "包含或者排除") Boolean include)
            throws Exception {

        try {
            Class enumClass = Class.forName(ClassUtils.getPackageName(DiscountType.class) + "." + StringUtils.capitalize(entity));
            return Result.ok(ArrayUtils.isEmpty(ids) ?
                    EnumConvertUtils.getList(enumClass) :
                    BooleanUtils.isTrue(include) ?
                            EnumConvertUtils.getListIn(enumClass, ids) :
                            EnumConvertUtils.getListNotIn(enumClass, ids));
        }
        catch (Exception e) {
            return new Result<>();
        }
    }
}
