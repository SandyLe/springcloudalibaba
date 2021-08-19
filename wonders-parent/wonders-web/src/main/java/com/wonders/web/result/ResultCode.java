package com.wonders.web.result;

import java.io.Serializable;


public enum ResultCode implements Serializable {

    //所有在自定义Code必须6位数字，成功默认200，不需要在这里定义，失败默认500不需在这里定义
    //业务上的错误则以600开头的6位数字
    API_CODE_ERROR(600001,"错误的API服务码！"),
    API_ACTION_ERROR(600002,"传入参数结构错误！"),
    API_PROHIBIT_ERROR(600003,"接口已禁用，请联系管理员！"),
    API_DDZBS_ERROR(600004,"resultType 错误！"),
    API_DDZ_ERROR(600005,"指标查询缺少必要参数条件！"),
    API_PARAM_ERROR(600006,"参数错误！"),
    API_RESULT_NOT_FOUND(600007,"无查询结果！"),
    //请求类错误必须以400开头的6位数字
    TOKEN_ILLEGAL(400001, "token失效！"),
    //文件错误相关CODE 以400100开始的6位数字
    FilE_ERROR_SAVE(400100, "文件保存出错，请检查保存路径是否正确！"),
    //程序错误必须以500开头的6位数字，例如500001,500002等等
    LECTURER_REQUISITION_REGISTERED(500001, "用户或密码错误！"),
    LECTURER_REQUISITION_LOCK(500002, "账号锁定，请30分钟后再次尝试！"),
    LECTURER_REQUISITION_WAIT(500003, "该用户没有权限！"),
    LECTURER_REQUISITION_LOG(500004, "用户日志插入异常！"),
    LOGIN_VERIFY_CODE(500005, "验证码错误，请重新输入！"),
    LOGIN_VERIFY_CODE_EXPIRE(500006, "验证码已过期，请重新获取验证码！"),
    LOGIN_DOMAIN_ERROR(500007, "错误的应用信息！"),
    LECTURER_REQUISITION_PASSWORDTIMESONE(500008, "密码输入错误！您还有1次机会！"),
    LECTURER_REQUISITION_PASSWORDTIMESTWO(500009, "密码输入错误！您还有2次机会！"),
    LECTURER_REQUISITION_NULL(500010, "用户不存在或手机号码有误！"),
    LOGIN_ERROR(500100, "登录错误！");
    private static final long serialVersionUID = -1223450118777099537L;
    private String msg;
    private int code;


    private ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
