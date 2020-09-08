package org.jeecg.modules.taobao.dto;

public class MsgTradeDto {

    private String store_code;

    private String buyer_nick;

    private String post_fee;

    private Long iid;

    private String payment;

    private Long oid;

    private String seller_nick;

    private String type;

    private Long tid;

    private String status;

    public String getStore_code() {
        return store_code;
    }

    public void setStore_code(String store_code) {
        this.store_code = store_code;
    }

    public String getBuyer_nick() {
        return buyer_nick;
    }

    public void setBuyer_nick(String buyer_nick) {
        this.buyer_nick = buyer_nick;
    }

    public String getPost_fee() {
        return post_fee;
    }

    public void setPost_fee(String post_fee) {
        this.post_fee = post_fee;
    }

    public Long getIid() {
        return iid;
    }

    public void setIid(Long iid) {
        this.iid = iid;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getSeller_nick() {
        return seller_nick;
    }

    public void setSeller_nick(String seller_nick) {
        this.seller_nick = seller_nick;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
