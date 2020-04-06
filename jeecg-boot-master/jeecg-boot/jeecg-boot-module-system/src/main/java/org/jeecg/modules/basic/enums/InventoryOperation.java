package org.jeecg.modules.basic.enums;

public enum InventoryOperation implements AbstractEnum{

    STOCKING(0, "库存盘点"),
    SALEOUT(1, "销售出库"),
    PURCHASEIN(2, "采购入库"),
    PURCHASERETURNOUT(3, "采购退货出库"),
    SALERETURNIN(4, "销售退货入库"),
    STOCKINGADD(5, "盘点库存调整增加"),
    STOCKINGSUB(6, "盘点库存调整减少"),
    ALLOTIN(7, "调拨入库"),
    ALLOTOUT(8, "调拨出库"),
    ASSEMBLEIN(9, "组装入库"),
    ASSEMBLEOUT(10, "组装出库"),
    TEARDOWNIN(11, "拆卸入库"),
    TEARDOWNOUT(12, "拆卸出库");

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
    public String getCode() {
        return null;
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
