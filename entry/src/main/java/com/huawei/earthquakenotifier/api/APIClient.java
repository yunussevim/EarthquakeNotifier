package com.huawei.earthquakenotifier.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    private static OkHttpClient getOkHttpClient(){
        if(okHttpClient == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            return okHttpClient;
        }
        else {
            return okHttpClient;
        }
    }

    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://earthquake.usgs.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofit;
    }

    public static Retrofit getAuthClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://oauth-login.cloud.huawei.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofit;
    }

    public static Retrofit getPushClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://push-api.cloud.huawei.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofit;
    }
}
