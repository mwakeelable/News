package com.raye7.news.utils;

import android.support.annotation.NonNull;

import com.raye7.news.data.network.news.NewsDataSource;
import com.raye7.news.data.network.news.NewsRepository;
import com.raye7.news.data.network.news.NewsRepositoryImp;
import com.raye7.news.data.network.news.remote.NewsDataSourceRemote;
import com.raye7.news.domain.GetNewsUseCase;
import com.raye7.news.domain.usecase.UseCaseHandler;

public class FactoryInjection {
    public static UseCaseHandler provideUseCaseHandler() {

        return UseCaseHandler.getInstance();
    }

    @NonNull
    public static GetNewsUseCase injectGetNewsUseCase() {

        return new GetNewsUseCase(injectNewsRepository());
    }

    private static NewsRepository injectNewsRepository() {

        return NewsRepositoryImp.getINSTANCE(injectNewsDataSource());
    }

    private static NewsDataSource injectNewsDataSource() {

        return NewsDataSourceRemote.getInstance();
    }

}
