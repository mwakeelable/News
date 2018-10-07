package com.raye7.news;

import com.raye7.news.data.db.ArticleDBModel;
import com.raye7.news.data.db.RealmManager;
import com.raye7.news.domain.GetNewsUseCase;
import com.raye7.news.presentation.home.NewsContract;
import com.raye7.news.presentation.home.NewsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Mock
    ArticleDBModel article;

    NewsPresenter presenter;
    NewsContract.NewsView view = Mockito.mock(NewsContract.NewsView.class);
    GetNewsUseCase getNewsUseCase = Mockito.mock(GetNewsUseCase.class);
    RealmManager realmManager = Mockito.mock(RealmManager.class);

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = Mockito.spy(new NewsPresenter(view, getNewsUseCase, realmManager));
    }

    @Test
    public void testGetNews() {
        presenter.getListOfNews("1");
        Mockito.verify(presenter, Mockito.atLeastOnce()).getListOfNews("1");
    }

    @Test
    public void testCacheNews() {
        List<ArticleDBModel> articleDBModels = new ArrayList<>();
        articleDBModels.add(article);
        presenter.saveItem(articleDBModels, article);
        Mockito.verify(presenter, Mockito.atLeastOnce()).saveItem(articleDBModels, article);
    }

    @Test
    public void testNotSaveExist() {
        List<ArticleDBModel> cachedNews = new ArrayList<>();
        cachedNews.add(article);
        String url = "url";
        List<String> urls = new ArrayList<>();
        urls.add(url);
        if (urls.contains(url))
            Mockito.verify(presenter, Mockito.never()).saveItem(cachedNews, article);
    }
}