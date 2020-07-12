package org.jeecg.common.enums;

/**
 * 地址类型
 */
public enum AddressType implements AbstractEnum {

    Delivery(0, "delivery", "送货地址"),
    Invoice(1, "invoice", "发票地址"),
    Install(2, "install", "安装地址")
    ;

    private Integer id;
    private String code;
    private String name;
    AddressType(Integer id, String code, String name){
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
        AddressType[] values = AddressType.values();
        for (AddressType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        AddressType[] values = AddressType.values();
        for (AddressType o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        AddressType[] values = AddressType.values();
        for (AddressType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
