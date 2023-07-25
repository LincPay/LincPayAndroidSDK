package com.lincpaydemo.Retrofit;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitRequest {

    private static Retrofit retrofit;


    public static Retrofit getRetrofitInstance(Context mContext) {

        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body())
                            .build();


                    ConnectivityManager cm =
                            (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    NetworkRequest.Builder requestBuilder=new NetworkRequest.Builder();
                    requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
                    requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                    cm.requestNetwork(requestBuilder.build(),new ConnectivityManager.NetworkCallback(){

                        @Override
                        public void onAvailable(Network network)
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                cm.bindProcessToNetwork(network);
                            }
                        }

                        @Override
                        public void onCapabilitiesChanged (Network network, NetworkCapabilities networkCapabilities)
                        {

                        }

                        @Override
                        public void onLinkPropertiesChanged (Network network, LinkProperties linkProperties)
                        {}

                        @Override
                        public void onLosing (Network network, int maxMsToLive)
                        {}

                        @Override
                        public void onLost (Network network)
                        {}

                    });

                    return chain.proceed(request);
                }
            })
                    .connectTimeout(80, TimeUnit.SECONDS)
                    .writeTimeout(80, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(0, 5, TimeUnit.MINUTES))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://merchant.swipelinc.com")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
