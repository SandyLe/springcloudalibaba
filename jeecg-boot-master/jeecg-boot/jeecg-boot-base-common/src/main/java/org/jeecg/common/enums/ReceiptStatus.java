package org.jeecg.common.enums;

/**
 * 收款状态
 */
public enum ReceiptStatus implements AbstractEnum {

    Obligation(0, "Obligation", "待付款"),
    PartialPayment(1, "PartialPayment", "部分支付"),
    Finished(2, "Finished", "支付完成")
    ;

    private Integer id;
    private String code;
    private String name;
    ReceiptStatus(Integer id, String code, String name){
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
        ReceiptStatus[] values = ReceiptStatus.values();
        for (ReceiptStatus o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        ReceiptStatus[] values = ReceiptStatus.values();
        for (ReceiptStatus o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        ReceiptStatus[] values = ReceiptStatus.values();
        for (ReceiptStatus o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
