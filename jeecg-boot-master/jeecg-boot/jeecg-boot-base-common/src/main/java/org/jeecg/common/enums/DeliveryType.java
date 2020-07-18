package org.jeecg.common.enums;

/**
 * 发货方式
 */
public enum DeliveryType implements AbstractEnum {

    Delivery(0, "TakeTheir", "自提"),
    Express(1, "Express", "快递"),
    ExpressSheet(2, "ExpressSheet", "电子面单"),
    Logistics(3, "Logistics", "物流")
    ;

    private Integer id;
    private String code;
    private String name;
    DeliveryType(Integer id, String code, String name){
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
        DeliveryType[] values = DeliveryType.values();
        for (DeliveryType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        DeliveryType[] values = DeliveryType.values();
        for (DeliveryType o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        DeliveryType[] values = DeliveryType.values();
        for (DeliveryType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
