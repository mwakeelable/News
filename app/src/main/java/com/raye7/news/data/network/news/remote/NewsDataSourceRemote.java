package com.raye7.news.data.network.news.remote;

import com.raye7.news.data.network.APIClient;
import com.raye7.news.data.network.news.NewsDataSource;
import com.raye7.news.data.network.news.models.NewsResponse;

import io.reactivex.Observable;

public class NewsDataSourceRemote implements NewsDataSource {
    private static NewsDataSourceRemote INSTANCE;

    public static NewsDataSourceRemote getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewsDataSourceRemote();
        } else {
            return INSTANCE;
        }
        return INSTANCE;

    }

    @Override
    public Observable<NewsResponse> getNewsFullResponse(String page) {
        Observable<NewsResponse> newsResponse = APIClient.getNewsWebService().getNews(
                "Google",
                "publishedAt",
                "003f6d92ba564cea89aefbe605a66b76",
                "USAToday",
                "en",
                page);
        return newsResponse;
    }
}
