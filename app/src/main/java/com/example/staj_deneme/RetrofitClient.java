package com.example.staj_deneme;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL="https://10.0.2.2:7296/";
    private static Retrofit retrofit=null;
    public static ApiDeneme getApiService(){

        if(retrofit==null){
            OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://10.0.2.2:7296/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiDeneme.class);
    }
    public static RecieveNotificationInterface getApiServiceNotification(){
        if(retrofit==null){
            OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://10.10.82.11:7296/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RecieveNotificationInterface.class);
    }

}
