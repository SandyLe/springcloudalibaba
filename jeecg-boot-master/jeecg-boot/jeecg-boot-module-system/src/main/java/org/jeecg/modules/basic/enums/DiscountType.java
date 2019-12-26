package org.jeecg.modules.basic.enums;

public enum DiscountType implements AbstractEnum{

    PERCENT("0", "百分比"),
    AMOUNT("1", "金额");

    private String id;
    private String name;
    DiscountType(String id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
