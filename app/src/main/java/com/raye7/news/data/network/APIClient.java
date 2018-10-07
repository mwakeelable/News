package com.raye7.news.data.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static NewsWebServices newsWebService;
    private static Retrofit retrofit;
    public final static String BASE_URL = "https://newsapi.org/v2/";

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        return okHttpClient;
    }

    private static Retrofit getRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofit;
    }

    public static NewsWebServices getNewsWebService() {
        if (newsWebService == null) {
            newsWebService = getRetrofit().create(NewsWebServices.class);
        } else {
            return newsWebService;
        }
        return newsWebService;
    }
}
