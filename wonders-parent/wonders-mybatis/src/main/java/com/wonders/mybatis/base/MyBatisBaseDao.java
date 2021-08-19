package com.wonders.mybatis.base;

import java.util.List;
import java.util.Map;

/**
 * @author wuxx
 * @date 2020/3/4 15:01
 */
public interface MyBatisBaseDao<T> {

    /**
     * 通用列表查询
     * @param map
     * @return
     */
    List<T> getList(Map<String, Object> map);
    /**
     * 根据id查询单个详情
     * @param obj 根据ID查询数据
     * @return T 返回查询对象
     */
    T getInfoById(Object obj);

    /**
     * 通用分页统计
     * @param params 查询参数
     * @return int
     */
    int getPageTotal(Map<String, Object> params);

    /**
     * 通用增加方法(单个)
     * @param obj 保存对象
     * @return int
     */
    int save(T obj);

    /**
     * 通用增加方法(多个)
     * @param obj 保存对象
     * @return int
     */
    int save(List<T> obj);
    /**
     * 通用修改方法
     * @param obj 根据对象Id修改
     * @return int
     */
    int update(T obj);
    /**
     * 通用修改方法
     * @param obj 根据多个参数条件修改
     * @return int
     */
    int update(Map<String, Object> obj);

    /**
     * 通用删除方法(单个)
     * @param obj 删除的Id标识
     * @return int
     */
    int deleteById(Object obj);
    /**
     * 通用删除方法(数组)
     * @param obj 删除的Id标识
     * @return int
     */
    int deleteByIds(Object[] obj);

}
