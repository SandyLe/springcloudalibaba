package org.jeecg.common.enums;

/**
 * 差价模式
 */
public enum PriceSpaceMode implements AbstractEnum {

    Receive(0, "Receive", "收款"),
    Refund(1, "Refund", "退款"),
    ;

    private Integer id;
    private String code;
    private String name;
    PriceSpaceMode(Integer id, String code, String name){
        this.id = id;
        this.code = code;
        this.name = name;
    }

    @Override
    public String getSid() {
        return String.valueOf(id);
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(Integer id) {
        PriceSpaceMode[] values = PriceSpaceMode.values();
        for (PriceSpaceMode o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        PriceSpaceMode[] values = PriceSpaceMode.values();
        for (PriceSpaceMode o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        PriceSpaceMode[] values = PriceSpaceMode.values();
        for (PriceSpaceMode o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
