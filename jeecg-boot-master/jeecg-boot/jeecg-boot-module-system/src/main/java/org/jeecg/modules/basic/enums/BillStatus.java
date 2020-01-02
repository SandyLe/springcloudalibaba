package org.jeecg.modules.basic.enums;

public enum BillStatus implements AbstractEnum{

    NEW(0, "草稿"),
    PARTICIALPAYMENT(1, "部分支付"),
    PAID(2, "支付完成"),
    TOSEND(3, "待发货"),
    PARTICIALSEND(4, "部分发货"),
    TOINSTALL(5, "待安装"),
    DOWN(6, "已完成");

    private Integer id;
    private String name;
    BillStatus(Integer id, String name){
        this.id = id;
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

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(Integer id) {
        BillStatus[] values = BillStatus.values();
        for (BillStatus o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }
}
