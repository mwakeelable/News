package com.raye7.news.data.network;

import com.raye7.news.data.network.news.models.NewsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsWebServices {
    @GET("everything")
    Observable<NewsResponse> getNews(@Query("q") String query,
                                     @Query("sortBy") String sortBy,
                                     @Query("apiKey") String apiKey,
                                     @Query("sources") String source,
                                     @Query("language") String lang,
                                     @Query("page") String page);
}
