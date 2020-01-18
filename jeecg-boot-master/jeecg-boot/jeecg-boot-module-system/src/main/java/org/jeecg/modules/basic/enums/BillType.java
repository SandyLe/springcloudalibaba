package org.jeecg.modules.basic.enums;

public enum BillType implements AbstractEnum{

    SALEORDER(0, "sale_order", "销售订单"),
    CUSTOMER(1, "customer", "客户"),
    VENDOR(2, "vendor", "供应商"),
    SALERETURNORDER(3, "sale_return_order", "销售退货"),
    PURCHASEORDER(4, "purchase_order", "采购订单"),
    PURCHASERETURNORDER(5, "purchase_return_order", "采购退货"),
    MATERIAL(6, "material", "产品"),
    STOCKING(7, "stocking", "库存盘点"),
    INVENTORY(8, "inventory", "库存单"),
    CUSTOMERSOURCE(9, "customerSource", "客户来源"),
    CUSTOMERTYPE(10, "customerType", "客户类型");

    private Integer id;
    private String code;
    private String name;
    BillType(Integer id, String code, String name){
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
        BillType[] values = BillType.values();
        for (BillType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        BillType[] values = BillType.values();
        for (BillType o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        BillType[] values = BillType.values();
        for (BillType o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
