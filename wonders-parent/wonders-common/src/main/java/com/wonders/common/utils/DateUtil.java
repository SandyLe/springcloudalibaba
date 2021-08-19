package com.wonders.common.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期相关处理
 * @author wuxx
 */
public class DateUtil {


    /**
     * Calendar.YEAR 年
     * Calendar.MONTH 月
     * Calendar.DAY_OF_MONTH 日
     * Calendar.HOUR_OF_DAY 时
     * Calendar.MINUTE 分
     * Calendar.SECOND 秒
     * 默认 当前时间毫秒数
     * @param format
     * @return
     */
    public static String getDa(int format){
        Calendar now = Calendar.getInstance();
        if(Calendar.ERA == format){
            return String.valueOf(now.getTimeInMillis());
        }
        if(Calendar.MONTH == format){
            return String.valueOf(now.get(Calendar.MONTH) + 1);
        }
       return String.valueOf(now.get(format));
    }

    public static String getDa(Date d, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        return sdf.format(d);
    }

    public static Date getDa(String dStr, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();

    }

}
