package org.jeecg.common.enums;

/**
 * 退货方式
 */
public enum ReturnType implements AbstractEnum {

    MoneyOnly(0, "MoneyOnly", "仅退款"),
    MtlMoney(1, "MtlMoney", "退货退款"),
    ;

    private Integer id;
    private String code;
    private String name;
    ReturnType(Integer id, String code, String name){
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
        ReturnType[] values = ReturnType.values();
        for (ReturnType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        ReturnType[] values = ReturnType.values();
        for (ReturnType o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        ReturnType[] values = ReturnType.values();
        for (ReturnType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
