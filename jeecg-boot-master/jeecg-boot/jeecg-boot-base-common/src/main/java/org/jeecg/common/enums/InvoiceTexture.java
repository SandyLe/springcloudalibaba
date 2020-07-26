package org.jeecg.common.enums;

/**
 * 发票材质
 */
public enum InvoiceTexture implements AbstractEnum {

    PaperInvoice(0, "paperInvoice", "纸质发票"),
    EInvoice(1, "eInvoice", "电子发票"),
    ;

    private Integer id;
    private String code;
    private String name;
    InvoiceTexture(Integer id, String code, String name){
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
        InvoiceTexture[] values = InvoiceTexture.values();
        for (InvoiceTexture o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        InvoiceTexture[] values = InvoiceTexture.values();
        for (InvoiceTexture o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        InvoiceTexture[] values = InvoiceTexture.values();
        for (InvoiceTexture o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
