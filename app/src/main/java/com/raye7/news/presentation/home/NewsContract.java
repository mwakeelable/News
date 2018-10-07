package com.raye7.news.presentation.home;

import com.raye7.news.data.network.news.models.NewsResponse;
import com.raye7.news.data.db.ArticleDBModel;
import com.raye7.news.presentation.base.BasePresenter;
import com.raye7.news.presentation.base.BaseView;

import java.util.List;

public class NewsContract {
    public interface NewsPresenter extends BasePresenter {
        void getListOfNews(String page);

        void saveItem(List<ArticleDBModel> cachedNews, ArticleDBModel article);

        List<ArticleDBModel> getCachedNews();
    }

    public interface NewsView extends BaseView<NewsPresenter> {
        void onGetListOfNewsSuccess(NewsResponse newsResponse);

        void onSaveItemSuccessful(String message);

        void updateCachedNewsList();

        void onSaveItemFailed(String message);
    }
}
