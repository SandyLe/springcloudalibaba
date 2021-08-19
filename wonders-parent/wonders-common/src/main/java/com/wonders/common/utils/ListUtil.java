package com.wonders.common.utils;

import java.util.HashSet;
import java.util.List;

/**
 * List工具类
 * @author wuxx
 */

public class ListUtil {
    /**
     * 合并集合中重复的元素
     * @param list
     * @return
     */
    public static List mergerRepeat(List list){
        HashSet set = new HashSet(list);
        //把List集合所有元素清空
        list.clear();
        //把HashSet对象添加至List集合
        list.addAll(set);
        return list;
    }
}
