package org.jeecg.modules.report.entity;

/**
 * @Description: 文本 统计，输出模型
 * @Author: tomkluas
 * @Date: 2020/6/30 10:57
 * @Version: V1.0
 */
public class ShowTypeLabelOutputBean {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String title;
    private String value;
}
