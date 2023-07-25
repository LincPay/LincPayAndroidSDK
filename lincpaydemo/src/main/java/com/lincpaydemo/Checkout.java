package com.lincpaydemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lincpaydemo.Retrofit.ApiRequestService;
import com.lincpaydemo.Retrofit.RetrofitRequest;
import com.lincpaydemo.callbacks.PaymentInitiatorResultListener;
import com.lincpaydemo.encryption.UtilEncryption;
import com.lincpaydemo.model.PaymentRequestResponse;
import com.lincpaydemo.model.TransactionStatusResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Checkout  {


    private String keyID;
    private Activity activity;

    public void sendPaymentRequest(Activity activity, String mid, String encryptedKey, JSONObject jsonObject, PaymentInitiatorResultListener listener){

        // calling service for server request
        Retrofit retrofitInstance = RetrofitRequest.getRetrofitInstance(activity);
        ApiRequestService requestService = retrofitInstance.create(ApiRequestService.class);
        // Steps
        // First check the keyId matched


        this.activity=activity;
        // Call Service for payment request and encrypt the data
        Log.e("@sendPaymentRequest payLoad","jsonObject.toString() "+jsonObject.toString());



        Map<String, Object> map = new HashMap<>();
       /* map.put("payload", "TgNUkTh9UkNWhlfH+VbLBobl6E8+Nv5XtL0BjaBS6qTgRmXseURE2eo0c36ic2GcTq4aoz6SGF62ydM1cntxon/wBaQ6cYh91pE7q+LjOt0NhecMgcRp5ty6SRrr/oGhgdyqp6u8B9zQQ42S8mOqj42ZSDuukLX6YwOGSsW87yoQplVaDKWUO7mqLiWFBDXpy8s0VKvczq9W1tPTndktgAJsUk762kSZpb7oa3bwLg/0CfEekgRJ8/fHQlCVvNch/0o9xh2rRZRwhH0UBgNtShaXq87MPbcwndZzgGC8tQShQh9oktpvrqPG9A6VmzwU");
        map.put("mid", "SWIPEPROD100007");*/
        map.put("payload", UtilEncryption.getPayLoad(jsonObject.toString(),encryptedKey));
        map.put("mid", mid);
        String decrypt=decrypt( UtilEncryption.getPayLoad(jsonObject.toString(),encryptedKey),encryptedKey);

        Log.e("@sendPaymentRequest payLoad","payLoad "+UtilEncryption.getPayLoad(jsonObject.toString(),encryptedKey));
        Log.e("@sendPaymentRequest payLoad","decrypt "+decrypt);

       requestService.sendPaymentRequest(map)
                .enqueue(new Callback<PaymentRequestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PaymentRequestResponse> call,
                                           @NonNull Response<PaymentRequestResponse> response) {

                        Log.e("@sendPaymentRequest onResponse","response "+response.raw().code());
                        Log.e("@sendPaymentRequest onResponse","response "+response.raw().message());

                        Log.e("@sendPaymentRequest onResponse","response "+ response.body());
                        if (response.isSuccessful()) {

                            // handle the response and revert to the caller.
                           listener.onSuccess(response.body());

                            //listener.onError("Error");*/
                        }
                    }


                    @Override
                    public void onFailure(@NonNull Call<PaymentRequestResponse> call, @NonNull Throwable t) {
                        Log.e("@sendPaymentRequest onFailure","response "+t.getLocalizedMessage());
                    }
                });



    }


    public void checkTransactionStatus(Activity activity, String mid, String txnId, PaymentInitiatorResultListener listener){

        // calling service for server request
        Retrofit retrofitInstance = RetrofitRequest.getRetrofitInstance(activity);
        ApiRequestService requestService = retrofitInstance.create(ApiRequestService.class);
        // Steps
        // First check the keyId matched


        this.activity=activity;
        // Call Service for payment request and encrypt the data



        Map<String, Object> map = new HashMap<>();
       /* map.put("payload", "TgNUkTh9UkNWhlfH+VbLBobl6E8+Nv5XtL0BjaBS6qTgRmXseURE2eo0c36ic2GcTq4aoz6SGF62ydM1cntxon/wBaQ6cYh91pE7q+LjOt0NhecMgcRp5ty6SRrr/oGhgdyqp6u8B9zQQ42S8mOqj42ZSDuukLX6YwOGSsW87yoQplVaDKWUO7mqLiWFBDXpy8s0VKvczq9W1tPTndktgAJsUk762kSZpb7oa3bwLg/0CfEekgRJ8/fHQlCVvNch/0o9xh2rRZRwhH0UBgNtShaXq87MPbcwndZzgGC8tQShQh9oktpvrqPG9A6VmzwU");
        map.put("mid", "SWIPEPROD100007");*/
        map.put("txnId", txnId);
        map.put("mid", mid);

        requestService.checkTransactionStatus(map)
                .enqueue(new Callback<TransactionStatusResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TransactionStatusResponse> call,
                                           @NonNull Response<TransactionStatusResponse> response) {

                        Log.e("@checkTransactionStatus onResponse","response "+response.body());
                        Log.e("@checkTransactionStatus onResponse","response "+response.errorBody());

                        Log.e("@checkTransactionStatus onResponse","response "+ response.message());
                        if (response.isSuccessful()) {

                            // handle the response and revert to the caller.
                            listener.onSuccess(response.body());

                            //listener.onError("Error");*/
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TransactionStatusResponse> call, @NonNull Throwable t) {
                        Log.e("@checkTransactionStatus onFailure","response "+t.getLocalizedMessage());
                    }
                });



    }

    public void callIntent(Activity activity,String html){
        Intent i = new Intent();
        // MUST instantiate android browser, otherwise it won't work (it won't find an activity to satisfy intent)
        i.setComponent(new ComponentName("com.android.browser", "com.android.browser.BrowserActivity"));
        i.setAction(Intent.ACTION_VIEW);

        // Replace params (if any replacement needed)

        // May work without url encoding, but I think is advisable
        // URLEncoder.encode replace space with "+", must replace again with %20
        String dataUri = "data:text/html," + URLEncoder.encode(html).replaceAll("\\+","%20");
        Log.e("onActivityResult ","URI CIde dataUri "+dataUri);

        i.setDataAndType(Uri.parse(dataUri),"application/json");
        activity.startActivity(i);
    }

    private String getEncryptedString(String parameterJson, String encryptedKey) {

        try {
            Key secretKey = setKey(encryptedKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return
                        Base64.getEncoder().encodeToString(cipher.doFinal(parameterJson.getBytes("UTF-8")));
            }
        } catch (Exception e) {
            Log.e("Exception getEncryptedString: ","Exception in encrypt() : encryptKey- " + encryptedKey + " : Message- " + e.getMessage());
        }


        return null;
    }

    private Key setKey(String encryptKey) {

        MessageDigest sha = null;
        try {
            byte[] key = encryptKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            Key secretKey = new SecretKeySpec(key, "AES");
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private String decrypt(String responseString, String encryptKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            Key secretKey = setKey(encryptKey);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return new
                        String(cipher.doFinal(Base64.getDecoder().decode(responseString)), "UTF-8");
            }
        } catch (Exception e) {
            Log.e("Exception decrypt","Exception in decryptResponse() encryptKey- " +encryptKey + " : Message- " + e.getMessage());
        }

        return null;
    }

    public String paymentRequestResponse(){

        // calling service for server request

        return "upi://pay?ver=01&mode=15&am=100.00&mam=100.00&cu=INR&pa=zene.anubhav@timecosmos&pn=aditya&mc=8299&tr=Zenex61676459674421637832&tn=QR%20SIT%20Testing&mid=ZENEX001&msid=ZENEX001-ANUBHAV&mtid=ZENEX001-001";


    }


    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public String getKeyID() {
        return keyID;
    }


}
