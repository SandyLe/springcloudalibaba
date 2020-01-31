package org.jeecg.modules.basic.enums;

public enum RowSts implements AbstractEnum{

    INVALID(-1, "失效"),
    NEW(0, "草稿"),
    EFFECTIVE(1, "有效"),
    DELETED(2, "删除");

    private Integer id;
    private String name;
    RowSts(Integer id, String name){
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

    @Override
    public String getCode() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(Integer id) {
        RowSts[] values = RowSts.values();
        for (RowSts o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }
}
