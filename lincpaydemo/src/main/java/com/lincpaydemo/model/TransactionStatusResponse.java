package com.lincpaydemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionStatusResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("statusCode")
    @Expose
    private String statsCode;

    @SerializedName("responseData")
    @Expose
    private List<Order> orderData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatsCode() {
        return statsCode;
    }

    public void setStatsCode(String statsCode) {
        this.statsCode = statsCode;
    }

    public List<Order> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<Order> orderData) {
        this.orderData = orderData;
    }
}
