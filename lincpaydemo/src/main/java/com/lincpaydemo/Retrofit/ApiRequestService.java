package com.lincpaydemo.Retrofit;

import com.lincpaydemo.model.PaymentRequestResponse;
import com.lincpaydemo.model.TransactionStatusResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ApiRequestService {

    @POST("/paymentrequest/android")
    Call<PaymentRequestResponse> sendPaymentRequest(
            @Body Map<String, Object> map);

    @POST("/api/v1/transaction/status-txnid")
    Call<TransactionStatusResponse> checkTransactionStatus(@Body Map<String, Object> map);
/*
    @FormUrlEncoded
    @POST("paymentrequest")
    Call<ResponseBody> sendPaymentRequest(@Field("payload") String payload,
                                          @Field("mid") String mid);*/
}
