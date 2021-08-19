package com.wonders.log.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wonders.common.utils.EmptyUtil;
import com.wonders.log.annotation.LogAnnotation;
import com.wonders.log.entity.AdminLog;
import com.wonders.log.service.AdminLogService;
import com.wonders.web.exception.CustomException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Before: 前置通知, 在方法执行之前执行
 * After: 后置通知, 在方法执行之后执行
 * AfterRunning: 返回通知, 在方法返回结果之后执行
 * AfterThrowing: 异常通知, 在方法抛出异常之后
 * Around: 环绕通知, 围绕着方法执行
 *
 * @ClassName：LogAopActionConfg
 * @Description：日志操作管理切面类
 * @Author：wuxx
 * @Date：2020/3/6 13:48
 */
@Aspect
@Component
public class LogAopActionConfig {
    private static final String LOG_TARGET_TYPE = "targetType";
    private static final String LOG_ACTION = "action";
    private static final String LOG_REMARK = "remark";

    private static ThreadLocal<AdminLog> logObj = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(LogAopActionConfig.class);

    @Resource
    private AdminLogService adminLogService;


    /**
     * 切入点  @annotation(logAnnotation)需要你输入你实现的是哪个注解类。
     */
    @Pointcut("@annotation(com.wonders.log.annotation.LogAnnotation)")
    public void pointCutMethod() {
    }

    @Before(value = "pointCutMethod()")
    public void doServiceBefore(JoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        AdminLog log = new AdminLog();
        log.setBeginTime(new Date(startTime));
        Map<String, String> map = null;
        try {
            //下面开始获取 targetType，remark，action
            map = getLogMark(joinPoint);
            log.setAction(map.get(LOG_ACTION));
            log.setTargetType(map.get(LOG_TARGET_TYPE));
            log.setRemake(map.get(LOG_REMARK));
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        //设置用户信息
        setUserInfo(joinPoint, log);
    }


    /**
     * 记录操作日志
     *
     * @param joinPoint
     * @throws CustomException
     */
    @AfterReturning(value = "pointCutMethod()", returning = "object")
    public void doServiceReturning(JoinPoint joinPoint, Object object) throws CustomException {
        AdminLog log = logObj.get();
        long finishTime = System.currentTimeMillis();
        log.setFinishTime(new Date(finishTime));
        log.setTimeInterval(log.getFinishTime().getTime() - log.getBeginTime().getTime());
        log.setCtTime(new Date(System.currentTimeMillis()));
        log.setState(1);
        adminLogService.save(log);
        //增加移除处理
        logObj.remove();
    }

    /**
     * 异常通知
     *
     * @param joinPoint
     * @throws CustomException
     */
    @AfterThrowing(value = "pointCutMethod()", throwing = "e")
    public void doServiceThrowing(JoinPoint joinPoint, Throwable e) throws CustomException {
        AdminLog log = logObj.get();
        long finishTime = System.currentTimeMillis();
        log.setErrorMsg(e.getMessage());
        //保存异常日志信息
        log.setFinishTime(new Date(finishTime));
        log.setTimeInterval(log.getFinishTime().getTime() - log.getBeginTime().getTime());
        log.setCtTime(new Date(System.currentTimeMillis()));
        log.setState(0);
        adminLogService.save(log);
        //增加移除处理
        logObj.remove();
    }

    /**
     * @Description： 设置用户信息
     * @Author：邬星星
     * @Date：2020/3/31
     */
    private void setUserInfo(JoinPoint joinPoint, AdminLog log) {
        Map<String, Object> joinPointInfo = getJoinPointInfoMap(joinPoint);
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ipAddress =EmptyUtil.isEmpty(request.getHeader("X-Forwarded-For")) ? request.getRemoteAddr()
                : request.getHeader("X-Forwarded-For");
        log.setOperator(ipAddress);
        log.setIpAddress(getCliectIp(request));
        log.setMethodName(joinPointInfo.get("methodName").toString());
        log.setMethodParams(joinPointInfo.get("paramMap").toString());
        logObj.set(log);
    }

    /**
     * 获取注解中信息
     *
     * @param joinPoint
     * @return
     * @throws ClassNotFoundException
     */
    private Map<String, String> getLogMark(JoinPoint joinPoint) throws ClassNotFoundException {
        Map<String, String> map = new HashMap<>();
        String methodName = joinPoint.getSignature().getName();
        String targetName = joinPoint.getTarget().getClass().getName();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                //注解类
                LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
                //注解类中写的值
                map.put(LOG_TARGET_TYPE, logAnnotation.targetType());
                map.put(LOG_ACTION, logAnnotation.action());
                map.put(LOG_REMARK, logAnnotation.remark());
            }
        }
        return map;
    }


    /**
     * 获取客户端ip地址
     *
     * @param request
     * @return
     */
    public static String getCliectIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!EmptyUtil.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!EmptyUtil.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    public static Map<String, Object> getJoinPointInfoMap(JoinPoint joinPoint) {
        Map<String, Object> joinPointInfo = new HashMap<>();
        String classPath = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        joinPointInfo.put("classPath", classPath);
        joinPointInfo.put("methodName", methodName);
        Map<String, Object> paramMap = new HashMap<>();
        //参数值
        Object[] paramsArgsValues = joinPoint.getArgs();
        //参数名称
        String[] paramsArgsNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        for (int i = 0; i < paramsArgsNames.length; i++) {
            paramMap.put(paramsArgsNames[i], paramsArgsValues[i]);
        }
        try {
            if (paramMap.containsKey("file")) {
                paramMap.remove("file");
            }
            if (paramMap.containsKey("request")) {
                paramMap.remove("request");
            }
            if (paramMap.containsKey("response")) {
                paramMap.remove("response");
            }
            joinPointInfo.put("paramMap", JSONObject.toJSONString(paramMap, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue));
        } catch (Exception e) {
            logger.error("日志参数格式化异常！", e);
        }
        return joinPointInfo;
    }

}