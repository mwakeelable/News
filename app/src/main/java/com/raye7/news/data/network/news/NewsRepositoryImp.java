package com.raye7.news.data.network.news;

import com.raye7.news.data.network.news.models.NewsResponse;

import io.reactivex.Observable;

public class NewsRepositoryImp implements NewsRepository {
    private static NewsDataSource newsDataSource;
    private static NewsRepository INSTANCE;

    private NewsRepositoryImp(NewsDataSource newsDataSource) {
        this.newsDataSource = newsDataSource;
    }

    public static NewsRepository getINSTANCE(NewsDataSource newsDataSource) {
        if (INSTANCE == null) {
            return new NewsRepositoryImp(newsDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<NewsResponse> getNewsFullResponse(String page) {
        Observable<NewsResponse> response = newsDataSource.getNewsFullResponse(page);
        return response;
    }
}
