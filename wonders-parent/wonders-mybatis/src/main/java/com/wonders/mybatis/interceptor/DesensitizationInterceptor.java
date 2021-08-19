package com.wonders.mybatis.interceptor;

import com.wonders.mybatis.annotation.Desensitization;
import com.wonders.mybatis.DesensitionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 拦截器配置
 * @author wuxx
 */
@Slf4j
@SuppressWarnings({ "rawtypes", "unchecked" })
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }), })
public class DesensitizationInterceptor implements Interceptor {

    //脱敏
    private boolean desensitization = true;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        //如果需要对结果脱敏，则执行
        if(desensitization) {
            if(result instanceof ArrayList<?>){
                List<?> list = (ArrayList<?>)result;
                result = this.desensitization(list);
            }else {
                result = this.desensitization(result);
            }
        }
        return result;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 对返回结果脱敏
     * @param list
     * @return
     */
    private List desensitization(List list) {
        Class cls = null;
        Field[] objFields = null;
        if(list != null && list.size()>0) {
            for(Object o:list) {
                if(cls == null) {
                    cls = o.getClass();
                    objFields = cls.getDeclaredFields();
                }

                if(objFields != null) {
                    for(Field field:objFields) {
                        Desensitization desensitization;

                        if("serialVersionUID".equals(field.getName())){
                            continue;
                        }
                        if (String.class != field.getType() || (desensitization = field.getAnnotation(Desensitization.class)) == null) {
                            continue;
                        }
                        field.setAccessible(true);
                        String value = null;
                        try {
                            value = field.get(o)!=null?field.get(o).toString():null;
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if(value == null)
                            continue;

                        List<String> regular;
                        DesensitionType type = desensitization.type();
                        switch (type) {
                            case CUSTOM:
                                regular = Arrays.asList(desensitization.attach());
                                break;
                            case TRUNCATE:
                                regular = truncateRender(desensitization.attach());
                                break;
                            default:
                                regular = Arrays.asList(type.getRegular());
                        }
                        if (regular.size() > 1) {
                            String match = regular.get(0);
                            String result = regular.get(1);
                            if (null != match && result != null && match.length() > 0) {
                                value =  value.replaceAll(match, result);
                                try {
                                    field.set(o, value);
                                } catch (IllegalArgumentException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }


        return list;
    }

//    public static void main(String[] args) {
//        System.out.println("411526199504123557".replaceAll("(?<=\\w{3})\\w(?=\\w{4})","*"));
//    }


    private Object desensitization(Object obj) {
        Class cls = null;
        Field[] objFields = null;
        if(obj != null) {

            if(cls == null) {
                cls = obj.getClass();
                objFields = cls.getDeclaredFields();

                if(objFields != null) {
                    for(Field field:objFields) {
                        Desensitization desensitization;

                        if("serialVersionUID".equals(field.getName())){
                            continue;
                        }
                        if (String.class != field.getType() || (desensitization = field.getAnnotation(Desensitization.class)) == null) {
                            continue;
                        }
                        field.setAccessible(true);
                        String value = null;
                        try {
                            value = field.get(obj)!=null?field.get(obj).toString():null;
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if(value == null){
                            continue;
                        }
                        List<String> regular;
                        DesensitionType type = desensitization.type();
                        switch (type) {
                            case CUSTOM:
                                regular = Arrays.asList(desensitization.attach());
                                break;
                            case TRUNCATE:
                                regular = truncateRender(desensitization.attach());
                                break;
                            default:
                                regular = Arrays.asList(type.getRegular());
                        }
                        if (regular.size() > 1) {
                            String match = regular.get(0);
                            String result = regular.get(1);
                            if (null != match && result != null && match.length() > 0) {
                                value =  (value).replaceAll(match, result);

                                try {
                                    field.set(obj, value);
                                } catch (IllegalArgumentException | IllegalAccessException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

        }


        return obj;
    }

    private List<String> truncateRender(String[] attachs) {
        List<String> regular = new ArrayList<>();
        if (null != attachs && attachs.length >1) {
            String rule = attachs[0];
            String size = attachs[1];
            String template, result;
            if ("0".equals(rule)) {
                template = "^(\\S{%s})(\\S+)$";
                result = "$1";
            } else if ("1".equals(rule)) {
                template = "^(\\S+)(\\S{%s})$";
                result = "$2";
            } else {
                return regular;
            }
            try {
                if (Integer.parseInt(size) > 0) {
                    regular.add(0, String.format(template, size));
                    regular.add(1, result);
                }
            } catch (Exception e) {
                log.warn("ValueDesensitizeFilter truncateRender size {} exception", size);
            }
        }
        return regular;
    }

    public boolean isDesensitization() {
        return desensitization;
    }

    public void setDesensitization(boolean desensitization) {
        this.desensitization = desensitization;
    }
}
