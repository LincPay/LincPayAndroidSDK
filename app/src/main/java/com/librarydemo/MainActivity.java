package com.librarydemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.librarydemo.databinding.ActivityMainBinding;
import com.lincpaydemo.Checkout;
import com.lincpaydemo.callbacks.PaymentInitiatorResultListener;
import com.lincpaydemo.model.PaymentRequestResponse;
import com.lincpaydemo.model.TransactionStatusResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private String mid="";
    private String txnId="";

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Instantiate Checkout
                 */
                Checkout checkout = new Checkout();
                checkout.setKeyID("<YOUR_KEY_ID>");

                /**
                 * Pass your payment options to the LincPay Checkout as a JSONObject
                 */
                try {
                    JSONObject options = new JSONObject();
                    options.put("emailId", binding.edEmail.getText().toString().trim());
                    options.put("mobileNo", binding.edMobileNumber.getText().toString().trim());
                    options.put("mid", binding.edMid.getText().toString());
                    options.put("enckey", binding.edenckey.getText().toString());
                    options.put("orderNo", binding.edorderNo.getText().toString().trim());
                    options.put("amount", binding.edAmount.getText().toString().trim());
                    options.put("currency", binding.edCurrency.getText().toString().trim());
                    options.put("txnReqType", binding.edRequestType.getText().toString().trim());
                    options.put("respUrl", "www.google.com");//pass amount in currency subunits
                    options.put("udf1", "");
                    options.put("udf2", "");
                    options.put("udf3", "");
                    options.put("udf4", "");
//                    "9503f982f1054a1a9fd891cf9ac2dc2c"

                    checkout.sendPaymentRequest(MainActivity.this,binding.edMid.getText().toString().trim(),binding.edenckey.getText().toString().trim(), options, new PaymentInitiatorResultListener() {
                        @Override
                        public void onSuccess(Object object) {
                            if (object instanceof PaymentRequestResponse) {
                                PaymentRequestResponse paymentRequestResponse= (PaymentRequestResponse) object;

                                Uri uri = Uri.parse(paymentRequestResponse.getUpiString());


                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(uri);
                                // will always show a dialog to user to choose an app
                                Intent chooser = Intent.createChooser(intent, "Pay with");

                                // check if intent resolves

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    chooser.putExtra("mid",paymentRequestResponse.getMid());
                                    chooser.putExtra("txnId",paymentRequestResponse.getTxnId());
                                    mid=paymentRequestResponse.getMid();
                                    txnId=paymentRequestResponse.getTxnId();
                                    startActivityForResult(chooser, 1, null);
                                }
                            }

                        }

                        @Override
                        public void onError(String message) {
                            Log.e(message, "Error in starting LincPay Checkout");
                        }
                    });

                } catch (JSONException e) {
                    Log.e("Exception", "Error in starting LincPay Checkout");
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Checkout checkout = new Checkout();

        checkout.checkTransactionStatus(MainActivity.this,mid,txnId, new PaymentInitiatorResultListener() {
            @Override
            public void onSuccess(Object object) {
                if(object instanceof TransactionStatusResponse){
                    TransactionStatusResponse response= (TransactionStatusResponse) object;
                    if(requestCode==1 && response.getOrderData()!=null && response.getOrderData().size()>0){

                        // Case Success: response.getOrderData().get(0).getTxnresponseCode()==200
                        // Case failed: response.getOrderData().get(0).getTxnresponseCode()==99
                        // Case Not Initiated: response.getOrderData().get(0).getTxnresponseCode()==198
                        binding.tvResponse.setText("Payment resposne: "+response.getOrderData().get(0).getTxnStatus()+" , Code: "+response.getOrderData().get(0).getTxnresponseCode());

                    }else {
                        binding.tvResponse.setText("Payment resposne: "+response.getMessage()+" , Code: "+response.getStatsCode());

                    }

                }

            }



            @Override
            public void onError(String message) {
                Log.e(message, "Error in Lincpay checkTransactionStatus");
            }
        });


    }
}