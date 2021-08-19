package com.wonders.common.key;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的类.
 *
 * @author wuxx
 * @version 2020-06-02
 */
public class IdGen {

    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.数据库主键ID
     */
    public static String uuid32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间有-分割.
     */
    public static String uuid36() {
        return UUID.randomUUID().toString();
    }

    /**
     * 使用SecureRandom随机生成Long
     *
     * @return long 长度随机
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 使用SecureRandom随机生成Long
     *
     * @return long 长度小于8位数
     */
    public static long randomLong8() {
        long a = Math.abs(random.nextLong());
        return Long.valueOf(String.valueOf(a).substring(0, 8));
    }

    /**
     * 使用SecureRandom随机生成Long
     *
     * @return long 长度小于8位数
     */
    public static int randomInt8() {
        long a = Math.abs(random.nextLong());
        return Integer.valueOf(String.valueOf(a).substring(0, 8));
    }

    /**
     * 使用SecureRandom随机生成Long
     *
     * @return long 长度小于16位数
     */
    public static long randomLong16() {
        long a = Math.abs(random.nextLong());
        return Long.valueOf(String.valueOf(a).substring(0, 16));
    }


}
