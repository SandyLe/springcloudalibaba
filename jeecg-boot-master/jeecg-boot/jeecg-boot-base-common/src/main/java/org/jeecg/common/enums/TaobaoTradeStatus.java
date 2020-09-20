package org.jeecg.common.enums;

/**
 * 收款状态
 */
public enum TaobaoTradeStatus implements AbstractEnum {

    WAIT_BUYER_PAY(0, "WAIT_BUYER_PAY", "等待买家付款"),
    PartialPayment(1, "WAIT_SELLER_SEND_GOODS", "等待卖家发货"),
    SELLER_CONSIGNED_PART(2, "SELLER_CONSIGNED_PART", "卖家部分发货"),
    WAIT_BUYER_CONFIRM_GOODS(3, "WAIT_BUYER_CONFIRM_GOODS", "等待买家确认收货"),
    TRADE_BUYER_SIGNED(4, "TRADE_BUYER_SIGNED", "买家已签收（货到付款专用）"),
    TRADE_FINISHED(5, "TRADE_FINISHED", "交易成功"),
    TRADE_CLOSED(6, "TRADE_CLOSED", "交易关闭"),
    TRADE_CLOSED_BY_TAOBAO(7, "TRADE_CLOSED_BY_TAOBAO", "交易被淘宝关闭"),
    TRADE_NO_CREATE_PAY(8, "TRADE_NO_CREATE_PAY", "没有创建外部交易（支付宝交易）"),
    WAIT_PRE_AUTH_CONFIRM(9, "WAIT_PRE_AUTH_CONFIRM", "余额宝0元购合约中"),
    PAY_PENDING(10, "PAY_PENDING", "外卡支付付款确认中"),
    ALL_WAIT_PAY(11, "ALL_WAIT_PAY", "所有买家未付款的交易"),
    ALL_CLOSED(12, "ALL_CLOSED", "所有关闭的交易"),
    PAID_FORBID_CONSIGN(13, "PAID_FORBID_CONSIGN", "禁止发货"),
    ;

    private Integer id;
    private String code;
    private String name;
    TaobaoTradeStatus(Integer id, String code, String name){
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
        TaobaoTradeStatus[] values = TaobaoTradeStatus.values();
        for (TaobaoTradeStatus o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static String getName(String code) {
        TaobaoTradeStatus[] values = TaobaoTradeStatus.values();
        for (TaobaoTradeStatus o: values){
            if(o.getMap().get("code").equals(code)){
                return o.getMap().get("name").toString();
            }
        }
        return null;
    }

    public static TaobaoTradeStatus getByCode(String code) {
        TaobaoTradeStatus[] values = TaobaoTradeStatus.values();
        for (TaobaoTradeStatus o: values){
            if(o.getMap().get("code").equals(code)){
                return o;
            }
        }
        return null;
    }

    public static String getCode(Integer id) {
        TaobaoTradeStatus[] values = TaobaoTradeStatus.values();
        for (TaobaoTradeStatus o: values){
            if(o.getMap().get("id").equals(id)){
                return o.getMap().get("code").toString();
            }
        }
        return null;
    }
}
