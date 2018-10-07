package com.raye7.news.domain;

import com.raye7.news.data.network.news.NewsRepository;
import com.raye7.news.data.network.news.models.NewsResponse;
import com.raye7.news.domain.base.BaseUseCase;

import io.reactivex.Observable;

public class GetNewsUseCase extends BaseUseCase<String, NewsResponse> {
    NewsRepository repository;

    public GetNewsUseCase(NewsRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable<NewsResponse> createObservableUseCase(String requestValues) {
        Observable<NewsResponse> response = repository.getNewsFullResponse(requestValues);
        return response;
    }
}
