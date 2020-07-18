package org.jeecg.common.enums;

/**
 * 收款单状态
 */
public enum ReceiptOrderStatus implements AbstractEnum {

    AgencyCollection(0, "AgencyCollection", "待收款"),
    PartialCollection(1, "PartialCollection", "部分收款"),
    Finished(2, "Finished", "收款完成")
    ;

    private Integer id;
    private String code;
    private String name;
    ReceiptOrderStatus(Integer id, String code, String name){
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
        ReceiptOrderStatus[] values = ReceiptOrderStatus.values();
        for (ReceiptOrderStatus o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        ReceiptOrderStatus[] values = ReceiptOrderStatus.values();
        for (ReceiptOrderStatus o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        ReceiptOrderStatus[] values = ReceiptOrderStatus.values();
        for (ReceiptOrderStatus o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
