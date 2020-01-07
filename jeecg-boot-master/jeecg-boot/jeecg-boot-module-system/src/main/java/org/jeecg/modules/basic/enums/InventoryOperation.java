package org.jeecg.modules.basic.enums;

public enum InventoryOperation implements AbstractEnum{

    STOCKING(0, "库存盘点"),
    SALEOUT(1, "销售出库"),
    PURCHASEIN(1, "采购入库");

    private Integer id;
    private String name;
    InventoryOperation(Integer id, String name){
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

    String getName(Integer id) {
        InventoryOperation[] values = InventoryOperation.values();
        for (InventoryOperation o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }
}
