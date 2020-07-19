package org.jeecg.modules.report.entity;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/30 10:58
 * @Version: V1.0
 */
public class ShowTypeRankingListOutputBean {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private String title;

    private Object data;
}
