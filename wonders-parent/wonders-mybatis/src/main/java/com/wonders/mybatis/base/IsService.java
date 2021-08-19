package com.wonders.mybatis.base;


import com.wonders.mybatis.page.PageBean;

import java.util.List;
import java.util.Map;

/**
 * @author wuxx
 * @date 2020/3/5 15:58
 */
public interface IsService<T> {

    /**
     * 通用列表查询
     * @param map
     * @return
     */
    List<T> getList(Map<String, Object> map);

    /**
     * 通用分页
     * @param map
     * @return
     */
    PageBean getPageList(Map<String, Object> map);
    /**
     * 获取详情
     * @param obj
     * @return
     */
    T getInfoById(Object obj);

    /**
     * 通用增加方法(单个)
     * @param obj 保存对象
     * @return boolean
     */
    int save(T obj);

    /**
     * 通用修改方法
     * @param obj
     * @return boolean
     */
    int update(T obj);

    /**
     * 通用删除方法(单个)
     * @param obj
     * @return int
     */
    int deleteById(Object obj);

    /**
     * 通用删除方法(多个)
     * @param obj 数组
     * @return int
     */
    int deleteByIds(Object[] obj);
}