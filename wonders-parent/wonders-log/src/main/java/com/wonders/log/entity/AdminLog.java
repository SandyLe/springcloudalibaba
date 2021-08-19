package com.wonders.log.entity;

import com.wonders.common.key.IdGen;
import com.wonders.common.utils.EmptyUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用日志标(SSealCurrentLog)实体类
 *
 * @author wuxingxing
 * @since 2021-03-07 12:48:33
 */
@Data
public class AdminLog implements Serializable {
    private static final long serialVersionUID = 968568293385629036L;

    private String logId;

    private String ipAddress;

    private String appId;

    private String action;

    private String targetType;

    private String remake;

    private String methodName;

    private String methodType;

    private String methodParams;

    private Date beginTime;

    private Date finishTime;

    private Long timeInterval;

    private Integer state;

    private String errorMsg;

    private String operator;

    private Date ctTime;

    private String spareOne;

    private String spareTwo;


    public void init() {
        if (EmptyUtil.isEmpty(getLogId())) setLogId(IdGen.uuid32());
        if (EmptyUtil.isEmpty(getCtTime())) setCtTime(new Date());
    }

}