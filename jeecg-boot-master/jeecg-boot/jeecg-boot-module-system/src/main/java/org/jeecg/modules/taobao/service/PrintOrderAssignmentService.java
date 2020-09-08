package org.jeecg.modules.taobao.service;

import org.jeecg.modules.taobao.entity.PrintOrderAssignment;

import java.util.List;

public interface PrintOrderAssignmentService {
    /**
     * 保存
     * @param printOrderAssignment
     */
    public boolean savePrintOrderAssignment(PrintOrderAssignment printOrderAssignment);
    /**
     * 批量保存
     * @param printOrderAssignmentList
     */
    public boolean savePrintOrderAssignmentBatch(List<PrintOrderAssignment> printOrderAssignmentList);
}
