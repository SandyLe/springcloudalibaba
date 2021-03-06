package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.basic.entity.BillCodeBuilder;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.DateFormat;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.modules.basic.mapper.BillCodeBuilderMapper;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillCodeBuilderServiceImpl extends ServiceImpl<BillCodeBuilderMapper, BillCodeBuilder> implements BillCodeBuilderService{

    @Autowired
    private BillCodeBuilderMapper billCodeBuilderMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String getBillCode(Integer billType) {

        return getBillCode(billType, LoginUtils.getLoginUser().getCompanyId(), LoginUtils.getLoginUser().getCompanyCode());
    }

    @Override
    public String getBillCode(Integer billType, String companyId, String companyCode) {
        String key = companyId + ":" + EnumConvertUtils.getOne(BillType.class, billType).getCode();
        Long value = 0l;
        LambdaQueryWrapper<BillCodeBuilder> lambdaQueryWrapper = new LambdaQueryWrapper<BillCodeBuilder>();
        lambdaQueryWrapper.eq(BillCodeBuilder::getBillType, billType);
        lambdaQueryWrapper.eq(BillCodeBuilder::getCompanyId, companyId);
        BillCodeBuilder bcb = getOne(lambdaQueryWrapper);
        if (null == bcb) {
            throw new JeecgBootException("【\"" +EnumConvertUtils.getName(BillType.class, billType) + "\"】编号器不存在，请核实!");
        }
        if (redisUtil.hasKey(key)) {
            value = redisUtil.incr(key, 1l);
        } else {
            value = bcb.getCurrentLevel() * 10 + 1l;
            redisUtil.set(key, value);
        }

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(companyCode)) {
            sb.append(companyCode);
        }
        if (StringUtils.isNotBlank(bcb.getPrefix())) {
            sb.append(bcb.getPrefix());
        }
        if (BooleanUtils.isTrue(bcb.getHasDate())) {
            sb.append(DateUtils.getDate(EnumConvertUtils.getName(DateFormat.class, bcb.getDateFmtId())));
        }
        if (bcb.getZeroCount() >0 ){
            sb.append(String.format("%0"+bcb.getZeroCount()+"d", value));
        }
        return sb.toString();
    }
}
