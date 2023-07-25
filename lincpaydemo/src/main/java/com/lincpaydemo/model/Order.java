package com.lincpaydemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("txn_id")
    @Expose
    private String txnId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("orderNo")
    @Expose
    private String orderNo;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("txnDate")
    @Expose
    private String txnDate;
    @SerializedName("txnRespCode")
    @Expose
    private String txnresponseCode;
    @SerializedName("txnStatus")
    @Expose
    private String txnStatus;
    @SerializedName("customerIfsc")
    @Expose
    private String customerIfsc;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnresponseCode() {
        return txnresponseCode;
    }

    public void setTxnresponseCode(String txnresponseCode) {
        this.txnresponseCode = txnresponseCode;
    }

    public String getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        this.txnStatus = txnStatus;
    }

    public String getCustomerIfsc() {
        return customerIfsc;
    }

    public void setCustomerIfsc(String customerIfsc) {
        this.customerIfsc = customerIfsc;
    }
}
