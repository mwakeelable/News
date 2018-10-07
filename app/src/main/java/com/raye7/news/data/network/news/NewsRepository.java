package com.raye7.news.data.network.news;

import com.raye7.news.data.network.news.models.NewsResponse;

import io.reactivex.Observable;

public interface NewsRepository {
    Observable<NewsResponse> getNewsFullResponse(String page);
}

