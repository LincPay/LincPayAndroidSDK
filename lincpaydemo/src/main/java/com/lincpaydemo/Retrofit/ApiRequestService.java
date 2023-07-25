package com.lincpaydemo.Retrofit;

import com.lincpaydemo.model.PaymentRequestResponse;
import com.lincpaydemo.model.TransactionStatusResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiRequestService {

    @POST("/paymentrequest/android")
    Call<PaymentRequestResponse> sendPaymentRequest(
            @Body Map<String, Object> map);

    @POST("/api/v1/transaction/status-txnid")
    Call<TransactionStatusResponse> checkTransactionStatus(@Body Map<String, Object> map);

}
