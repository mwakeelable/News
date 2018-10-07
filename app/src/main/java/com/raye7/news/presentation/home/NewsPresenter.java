package com.raye7.news.presentation.home;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.raye7.news.data.network.news.models.NewsResponse;
import com.raye7.news.data.db.ArticleDBModel;
import com.raye7.news.data.db.RealmManager;
import com.raye7.news.domain.GetNewsUseCase;
import com.raye7.news.domain.base.BaseUseCaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsPresenter implements NewsContract.NewsPresenter {
    private NewsContract.NewsView view;
    private GetNewsUseCase getNewsUseCase;
    private RealmManager realmManager;

    public NewsPresenter(NewsContract.NewsView view, GetNewsUseCase getNewsUseCase, RealmManager realmManager) {
        this.view = view;
        this.getNewsUseCase = getNewsUseCase;
        this.realmManager = realmManager;
    }

    @Override
    public void getListOfNews(String page) {
        if (view.isActive()) {
            view.setLoadingIndicator(true);
            getNewsUseCase.execute(page,
                    AndroidSchedulers.mainThread(),
                    Schedulers.io(),
                    new GetNewsObserver(view));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void saveItem(List<ArticleDBModel> cachedNews, final ArticleDBModel article) {
        if (view.isActive()) {
            if (realmManager.getRealm().isEmpty()) {
                article.setId(1);
                realmManager.saveNews(article);
                view.onSaveItemSuccessful("Saved...");
                view.updateCachedNewsList();
            } else {
                String url = article.getUrl();
                List<String> urls = new ArrayList<>();
                for (int i = 0; i < cachedNews.size(); i++) {
                    urls.add(cachedNews.get(i).getUrl());
                }
                if (urls.contains(url)) {
                    view.onSaveItemFailed("Already saved...");
                } else {
                    article.setId(realmManager.getDeliveryItems().size() + 1);
                    realmManager.saveNews(article);
                    view.onSaveItemSuccessful("Saved...");
                    view.updateCachedNewsList();
                }
            }
        }
    }

    @Override
    public List<ArticleDBModel> getCachedNews() {
        return realmManager.getDeliveryItems();
    }

    private class GetNewsObserver extends BaseUseCaseObserver<NewsResponse> {
        public GetNewsObserver(NewsContract.NewsView view) {
            super(view);
        }

        @Override
        protected void onSuccess(NewsResponse newsResponse) {
            view.setLoadingIndicator(false);
            view.onGetListOfNewsSuccess(newsResponse);
        }
    }

    @Override
    public void start() {

    }
}
