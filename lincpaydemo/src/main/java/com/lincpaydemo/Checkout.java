package com.lincpaydemo;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.lincpaydemo.Retrofit.ApiRequestService;
import com.lincpaydemo.Retrofit.RetrofitRequest;
import com.lincpaydemo.callbacks.PaymentInitiatorResultListener;
import com.lincpaydemo.encryption.UtilEncryption;
import com.lincpaydemo.model.PaymentRequestResponse;
import com.lincpaydemo.model.TransactionStatusResponse;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Checkout  {


    private String keyID;

    public void sendPaymentRequest(Activity activity, String mid, String encryptedKey, JSONObject jsonObject, PaymentInitiatorResultListener listener){

        // calling service for server request
        Retrofit retrofitInstance = RetrofitRequest.getRetrofitInstance(activity);
        ApiRequestService requestService = retrofitInstance.create(ApiRequestService.class);

        Map<String, Object> map = new HashMap<>();
        map.put("payload", UtilEncryption.getPayLoad(jsonObject.toString(),encryptedKey));
        map.put("mid", mid);

       requestService.sendPaymentRequest(map)
                .enqueue(new Callback<PaymentRequestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PaymentRequestResponse> call,
                                           @NonNull Response<PaymentRequestResponse> response) {
                        if (response.isSuccessful()) {
                            // handle the response and revert to the caller.
                           listener.onSuccess(response.body());
                        }else {
                            listener.onError(response.message());
                        }
                    }


                    @Override
                    public void onFailure(@NonNull Call<PaymentRequestResponse> call, @NonNull Throwable t) {
                        listener.onError(t.getMessage());
                    }
                });



    }


    public void checkTransactionStatus(Activity activity, String mid, String txnId, PaymentInitiatorResultListener listener){

        // calling service for server request
        Retrofit retrofitInstance = RetrofitRequest.getRetrofitInstance(activity);
        ApiRequestService requestService = retrofitInstance.create(ApiRequestService.class);



        Map<String, Object> map = new HashMap<>();
        map.put("txnId", txnId);
        map.put("mid", mid);

        requestService.checkTransactionStatus(map)
                .enqueue(new Callback<TransactionStatusResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TransactionStatusResponse> call,
                                           @NonNull Response<TransactionStatusResponse> response) {

                        if (response.isSuccessful()) {
                            // handle the response and revert to the caller.
                            listener.onSuccess(response.body());
                        }else {
                            listener.onError(response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TransactionStatusResponse> call, @NonNull Throwable t) {
                        listener.onError(t.getMessage());
                    }
                });



    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public String getKeyID() {
        return keyID;
    }


}
