package com.wonders.mybatis.page;


import lombok.Data;

import java.util.List;

/**
 * 分页查询返回结果封装类
 * @author wuxx
 * @date 2020/3/5 14:53
 */
@Data
public class PageBean<T> {
    /**总纪录数*/
    private int totalCount;
    /**总页码*/
    private int totalPage;
    /**每页的数据*/
    private List<T> list;
    /**当前的页码*/
    private int currentPage;
    /**每页显示的纪录数*/
    private int rows;

    /**
     *
     * @param list
     * @param total
     * @param query
     */
    public PageBean(List<T> list,int total,Query query) {
        this.list = list;
        this.totalCount = total;
        this.rows = query.getLimit();
        this.totalPage = (this.totalCount +this.rows - 1) / this.rows;
        this.currentPage = (query.getOffset() / this.rows)+1;
    }

}