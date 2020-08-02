package org.jeecg.common.enums;

public enum BillType implements AbstractEnum {

    SALEORDER(0, "saleOrder", "销售订单"),
    CUSTOMER(1, "customer", "客户"),
    VENDOR(2, "vendor", "供应商"),
    SALERETURNORDER(3, "saleOrderReturn", "销售退货"),
    PURCHASEORDER(4, "purchase", "采购订单"),
    PURCHASERETURNORDER(5, "purchaseReturn", "采购退货"),
    MATERIAL(6, "material", "产品"),
    STOCKING(7, "stocking", "库存盘点"),
    INVENTORY(8, "inventory", "库存单"),
    CUSTOMERSOURCE(9, "customerSource", "客户来源"),
    CUSTOMERTYPE(10, "customerType", "客户类型"),
    INVENTORYIN(11, "inventoryIn", "入库单"),
    INVENTORYOUT(12, "inventoryOut", "出库单"),
    ALLOT(13, "allot", "调拨单"),
    ASSEMBLE(14, "assemble", "组装单"),
    TEARDOWN(15, "teardown", "拆卸单"),
    WORKORDER(16, "workOrder", "工单"),
    REPAIR(17, "repairOrder", "维修单"),
    CHANGEORDER(18, "changeOrder", "换货单"),
    LOGISTICSORDER(19, "logisticsOrder", "物流单"),
    SYSROLE(20, "sysRole", "系统角色"),
    MATERIALTYPE(21, "materialType", "产品类型"),
    MATERIALBRAND(22, "materialBrand", "产品品牌"),
    MATERIALUNIT(23, "materialUnit", "产品单位"),
    EXPENSE(24, "expense", "费用名称"),
    LOGISTICSCOMPANY(25, "logisticsCompany", "物流公司"),
    WAREHOUSE(26, "warehouse", "仓库"),
    PURCHASEBATCH(27, "purchaseBatch", "采购批次"),
    BILLCODEBUILDER(27, "billCodeBuilder", "单据编号"),
    MATERIALPRICE(28, "materialPrice", "产品定价"),
    PURCHASECOST(29, "purchaseCost", "采购成本"),
    SYSPOSITION(30, "sysPosition", "职务"),
    ANNOUNTCEMENT(31, "annountCement", "通告"),
    SERVICEINSTITUTION(32, "serviceInstitution", "服务机构"),
    RECEIPTORDER(33, "ReceiptOrder", "收款单"),
    PAYMENTORDER(34, "PaymentOrder", "付款单"),
    INVOICE(35, "invoice", "发票"),
    REFUND(36, "refund", "退款单")
    ;

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
