package com.lincpaydemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentRequestResponse implements Serializable {
    @SerializedName("qrString")
    @Expose
    private String upiString;
    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("txnId")
    @Expose
    private String txnId;

    public String getUpiString() {
        return upiString;
    }

    public void setUpiString(String upiString) {
        this.upiString = upiString;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
