package com.wonders.mybatis.page;



import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分页查询参数封装工具类
 * @author wuxx
 * @date 2020/3/5 14:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Query extends LinkedHashMap<String,Object> implements Serializable {
    private static final long serialVersionUID = -3198048449643774660L;
    /** 页码 */
    public static final String PAGE = "page";
    /** 每页多少条 */
    public static final String LIMIT = "limit";
    /** 计算后sql参数 */
    public static final String OFFSET = "offset";
    /** 排序字段 */
    public static final String SORT = "sortField";
    /**
     * 开始位置，从0开始
     */
    private int offset;
    /**
     * 每页显示记录的条数
     */
    private int limit;
    /**
     * 排序字段 默认ct_time
     */
    private String sortField;

    /**
     * 排序顺序 默认倒叙
     */
    private String sortingOrder;

    /**
     * 封装分页查询参数
     * 默认排序字段
     * 正序还是倒序
     * @param params
     */
    public Query(Map<String,Object> params) {
        this.putAll(params);
        //页码默认为1
        int page = 1;
        if(params.containsKey(PAGE)){
            page = Integer.parseInt(params.get(PAGE).toString());
        }
        //每页数据默认10条
        this.limit = 10;
        if(params.containsKey(PAGE)){
            this.limit = Integer.parseInt(params.get(LIMIT).toString());
        }
        this.put(LIMIT,this.limit);
        this.offset =(page - 1) * this.limit;
        if(this.offset <= 0){
            this.offset = 0;
        }
        this.put(OFFSET,this.offset);

        //判断是否含有排序字段，如果有无正序或倒序的情况下默认正序
        // 如果不传默认使用表创建时间字段排序
        this.sortField = "ctTime";
        if(params.containsKey(SORT)){
            //TODO 防注入处理
            String sort = String.valueOf(params.get(SORT));
//            if(sort.length()>20 || sort.contains("and")){
//                throw new SQLException("排序字段异常");
//            }
            //将前台获取的属性名称转为下划线结构的表的字段
            this.sortField = this.camelToUnderline(sort,2);
            this.put(SORT, this.sortField);
        }
        //默认倒序，传0表示正序
        this.sortingOrder = "desc";
        if(params.containsKey(SORT) && "0".equals(params.get("order"))){
            //正序排序
            this.sortingOrder = "asc";
            this.put("sortingOrder", this.sortingOrder);
        }else if(params.containsKey(SORT) && "1".equals(params.get("order"))){
            //倒叙排序
            this.put("sortingOrder", this.sortingOrder);
        }
    }


    /**
     * 功能：驼峰转下划线。<br/>
     * 如果这个字符串为null或者trim后为空字符串则返回true，否则返回false。
     *
     * @date 2020年04月24日
     * @param param 要转化的字符
     * @param charType 2 表示大写, 其他情况都是小写
     * @return boolean
     */
    public  String camelToUnderline(String param, Integer charType) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
            }
            if (charType == 2) {
                //统一都转大写
                sb.append(Character.toUpperCase(c));
            } else {
                //统一都转小写
                sb.append(Character.toLowerCase(c));
            }


        }
        return sb.toString();
    }

}