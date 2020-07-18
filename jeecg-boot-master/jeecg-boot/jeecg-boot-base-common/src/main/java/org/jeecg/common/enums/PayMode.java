package org.jeecg.common.enums;

/**
 * 付款方式
 */
public enum PayMode implements AbstractEnum {

    Cash(0, "Cash", "现金"),
    Transfer(1, "Transfer", "银行转账"),
    Alipay(2, "Alipay", "支付宝"),
    WeChat(2, "WeChat", "微信支付"),
    ;

    private Integer id;
    private String code;
    private String name;
    PayMode(Integer id, String code, String name){
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
        PayMode[] values = PayMode.values();
        for (PayMode o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        PayMode[] values = PayMode.values();
        for (PayMode o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        PayMode[] values = PayMode.values();
        for (PayMode o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
