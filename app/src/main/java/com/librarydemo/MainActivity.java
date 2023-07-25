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
    //    setContentView(R.layout.activity_main);

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
                 * Pass your payment options to the Razorpay Checkout as a JSONObject
                 */
                try {
                    JSONObject options = new JSONObject();
                    //upi://pay?
                    // ver=01
                    // &mode=15
                    // &am=100.00
                    // &mam=100.00
                    // &cu=INR
                    // &pa=zene.anubhav@timecosmos
                    // &pn=aditya&mc=8299
                    // &tr=Zenex61676459674421637832
                    // &tn=QR%20SIT%20Testing
                    // &mid=ZENEX001
                    // &msid=ZENEX001-ANUBHAV
                    // &mtid=ZENEX001-001
                    options.put("emailId", binding.edEmail.getText().toString().trim());
                    options.put("mobileNo", binding.edMobileNumber.getText().toString().trim());
                    options.put("mid", binding.edMid.getText().toString());
                    options.put("enckey", binding.edenckey.getText().toString());
                    options.put("orderNo", binding.edorderNo.getText().toString().trim());
                    options.put("amount", binding.edAmount.getText().toString().trim());//from response of step 3.
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
                                Log.e("message", "Success in starting Razorpay Checkout" + new Gson().toJson(paymentRequestResponse));
                                Uri uri = Uri.parse(paymentRequestResponse.getUpiString());


                                Log.e("onActivityResult ", "URI CIde " + uri);
              /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(browserIntent);*/
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(uri);
                                //startActivity(intent);
                                // will always show a dialog to user to choose an app
                                Intent chooser = Intent.createChooser(intent, "Pay with");

                                // check if intent resolves
              /*  if(null != chooser.resolveActivity(getPackageManager())) {
                    startActivityForResult(chooser, 1);
                } else {
                    Toast.makeText(GatewayActivity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
                }*/
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
                            Log.e(message, "Error in starting Razorpay Checkout");
                        }
                    });

                } catch (JSONException e) {
                    Log.e("Exception", "Error in starting Razorpay Checkout");
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
                    Log.e("message", "Success in starting Razorpay Checkout"+new Gson().toJson(response));
                    if(response.getOrderData()!=null && response.getOrderData().size()>0){

                        binding.tvResponse.setText("Payment resposne: "+response.getOrderData().get(0).getTxnStatus()+" , Code: "+response.getOrderData().get(0).getTxnresponseCode());

                    }else {
                        binding.tvResponse.setText("Payment resposne: "+response.getMessage()+" , Code: "+response.getStatsCode());

                    }

                }

            }



            @Override
            public void onError(String message) {
                Log.e(message, "Error in starting Razorpay Checkout");
            }
        });


    }
}