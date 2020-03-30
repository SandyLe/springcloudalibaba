package org.jeecg.modules.quartz.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.puppycrawl.tools.checkstyle.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;

import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.jsonschema.CommonProperty;
import org.jeecg.modules.basic.entity.Warehouse;
import org.jeecg.modules.basic.service.WarehouseService;
import org.jeecg.modules.message.websocket.WebSocket;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.service.ISysLogService;
import org.jeecg.modules.system.service.ISysUserService;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class OrderDeliveryNoticeJob implements Job {

    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private ISysAnnouncementService sysAnnouncementService;
    @Autowired
    private ISysAnnouncementSendService sysAnnouncementSendService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private WarehouseService warehouseService;
    @Resource
    private WebSocket webSocket;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        int minutes = jobExecutionContext.getMergedJobDataMap().getIntegerFromString("parameter");
        log.info(String.format("定时触发发货通知! Job参数分钟="+minutes+" ，时间:" + DateUtils.getTimestamp()));

        Date date= new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);

        List<Warehouse> warehouseList = warehouseService.list();
        List<SysUser> userList = sysUserService.list();
        List<SaleOrder> list = saleOrderService.query().le("delivery_time", calendar.getTime()).ge("delivery_time", date).list();
        List<SysAnnouncement> sysAnnouncementList = sysAnnouncementService.list();

        String user = "admin";
        SysAnnouncement sysAnnouncement;
        SysAnnouncementSend announcementSend;
        for (SaleOrder saleOrder:list){
            try {
                List<SysAnnouncement> exitSysAnnouncements = sysAnnouncementList.stream()
                        .filter(p-> p.getMsgContent() != null && p.getMsgContent().contains(saleOrder.getId()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(exitSysAnnouncements)){
                    HashSet<String> destUser = new HashSet<>();
                    // 销售员ID
                    List<SysUser> u = userList.stream().filter(p-> Objects.equals(p.getUsername(), saleOrder.getCreateBy())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(u)){
                        destUser.add(u.get(0).getId());
                    }
                    // 仓管员ID
                    List<Warehouse> warehouse = warehouseList.stream().filter(p-> Objects.equals(p.getId() , saleOrder.getWarehouseId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(warehouse)){
                        destUser.add(warehouse.get(0).getPrincipalId());
                    }
//                System.out.println(String.format("Hash列表= %s", destUser.toString()));
                    sysAnnouncement = new SysAnnouncement();
                    sysAnnouncement.setTitile(String.format("您有未发货订单", saleOrder.getId()));
                    sysAnnouncement.setMsgContent(String.format("您的订单号[%s]尚未发货，请及时处理", saleOrder.getId()));
                    sysAnnouncement.setStartTime(date);
                    sysAnnouncement.setEndTime(saleOrder.getDeliveryTime());
                    sysAnnouncement.setSender(user);
                    sysAnnouncement.setPriority(CommonConstant.PRIORITY_M);         //L低，M中，H高
                    sysAnnouncement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);    //2:系统消息
                    sysAnnouncement.setMsgType(CommonConstant.MSG_TYPE_UESR);   // USER:指定用户
                    sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1); //0未发布，1已发布，2已撤销
                    sysAnnouncement.setSendTime(date);
                    sysAnnouncement.setUserIds(String.format("%s,", destUser.toString()));
                    sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());   //0 正常， 1 已删除
                    sysAnnouncement.setCreateBy(user);
                    sysAnnouncement.setCreateTime(date);
                    sysAnnouncement.setUpdateTime(date);
                    sysAnnouncement.setUpdateBy(user);
                    sysAnnouncementService.save(sysAnnouncement);

                    if (CollectionUtils.isNotEmpty(destUser)){
                        for (String uid : destUser){
                            // 1.将系统消息补充到用户通告阅读标记表中
                            announcementSend = new SysAnnouncementSend();
                            announcementSend.setAnntId(sysAnnouncement.getId());
                            announcementSend.setUserId(uid);
                            announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                            sysAnnouncementSendService.save(announcementSend);
                        }
                        String[] userArray = new String[destUser.size()];
                        JSONObject obj = new JSONObject();
                        obj.put("cmd", "user");
                        obj.put("msgId", sysAnnouncement.getId());
                        obj.put("msgTxt", sysAnnouncement.getTitile());
                        webSocket.sendMoreMessage(destUser.toArray(userArray), obj.toJSONString());
                    }
                }
            }
            catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }
}
