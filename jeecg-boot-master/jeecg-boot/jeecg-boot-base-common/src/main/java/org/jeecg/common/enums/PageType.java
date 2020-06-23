package org.jeecg.common.enums;

import java.util.Map;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/23 14:27
 * @Version: V1.0
 */
public enum PageType implements AbstractEnum {

    DASHBOARD(0, "DASHBOARD", "首页面板");

    private Integer id;
    private String code;
    private String name;

    PageType(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }


    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getSid() {
        return String.valueOf(id);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static String getName(Integer id) {
        PageType[] values = PageType.values();
        for (PageType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        PageType[] values = PageType.values();
        for (PageType o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        PageType[] values = PageType.values();
        for (PageType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }

}
