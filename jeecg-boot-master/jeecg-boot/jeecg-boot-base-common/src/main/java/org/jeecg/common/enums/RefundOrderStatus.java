package org.jeecg.common.enums;

/**
 * 退款单状态
 */
public enum RefundOrderStatus implements AbstractEnum {

    ToRefund(0, "ToRefund", "待退款"),
    PartialRefund(1, "PartialRefund", "部分退款"),
    Finished(2, "Finished", "退款完成")
    ;

    private Integer id;
    private String code;
    private String name;
    RefundOrderStatus(Integer id, String code, String name){
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
        RefundOrderStatus[] values = RefundOrderStatus.values();
        for (RefundOrderStatus o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        RefundOrderStatus[] values = RefundOrderStatus.values();
        for (RefundOrderStatus o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        RefundOrderStatus[] values = RefundOrderStatus.values();
        for (RefundOrderStatus o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
