package org.jeecg.modules.report.entity;

import java.util.List;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/30 11:01
 * @Version: V1.0
 */
public class PageReportOutputBean {
    public List<ShowTypeLabelOutputBean> getShowTypeLabelOutputBeans() {
        return showTypeLabelOutputBeans;
    }

    public void setShowTypeLabelOutputBeans(List<ShowTypeLabelOutputBean> showTypeLabelOutputBeans) {
        this.showTypeLabelOutputBeans = showTypeLabelOutputBeans;
    }

    public List<ShowTypeRankingListOutputBean> getShowTypeRankingListOutputBeans() {
        return showTypeRankingListOutputBeans;
    }

    public void setShowTypeRankingListOutputBeans(List<ShowTypeRankingListOutputBean> showTypeRankingListOutputBeans) {
        this.showTypeRankingListOutputBeans = showTypeRankingListOutputBeans;
    }

    private List<ShowTypeLabelOutputBean> showTypeLabelOutputBeans;

    private List<ShowTypeRankingListOutputBean> showTypeRankingListOutputBeans;

}
