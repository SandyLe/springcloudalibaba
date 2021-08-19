package com.wonders.web.util;

import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理配置文件
 * @author wuxx
 */
public class PropertiesUtil {


    /**
     * 处理yml 文件中的数组（适用无法使用自动注入的情况下）
     * @param env
     * @return
     */
    public static String[] getYmlArr(Environment env,String name){
        //标记
        boolean flag = true;
        //数组下标
        int i = 0;
        name = name.trim();
        List<String> list = new ArrayList<>();
        String key = "";
        while (flag){
            key = name+"[" + i + "]";
            if(env.containsProperty(key)){
                list.add(env.getProperty(key));
                i += 1;
            }else {
                flag = false;
            }
        }
        String[] strings = new String[list.size()];
         list.toArray(strings);
        return strings;
    }
}
