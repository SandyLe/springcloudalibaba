package org.jeecg.modules.report.entity;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/29 17:44
 * @Version: V1.0
 */
public class ReportForCompanyInputBean {
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String[] getReportids() {
        return reportids;
    }

    public void setReportids(String[] reportids) {
        this.reportids = reportids;
    }

    private String companyId;

    private String[] reportids;
}
