package com.raye7.news.presentation.home;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.raye7.news.R;
import com.raye7.news.data.network.news.models.NewsResponse;
import com.raye7.news.data.db.ArticleDBModel;
import com.raye7.news.data.db.RealmManager;
import com.raye7.news.utils.FactoryInjection;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NewsContract.NewsView,
        NewsAdapter.NewsClickInteractionListener,
        NewsAdapter.FavButtonClickListener, View.OnClickListener {

    List<ArticleDBModel> favList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        doInjection();
        initViews();
        if (!realmManager.getRealm().isEmpty()) {
            favList = newsPresenter.getCachedNews();
        }
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    RecyclerView newsList;
    NewsAdapter newsAdapter;
    ProgressBar progressBar;
    int limit = 20;
    int page = 1;
    boolean loadMore = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    NewsResponse newsResponse = new NewsResponse();
    private RealmManager realmManager;
    RadioButton allNewsBtn, favBtn;
    TextView pageTitle, favPlaceholderTxt;

    private void initViews() {
        mLayoutManager = new LinearLayoutManager(this);
        progressBar = findViewById(R.id.progress);
        pageTitle = findViewById(R.id.page_title_txt);
        favPlaceholderTxt = findViewById(R.id.fav_placeholder_txt);
        pageTitle.setText(getResources().getString(R.string.all_news));
        newsList = findViewById(R.id.newsList);
        newsList.setHasFixedSize(true);
        newsList.setLayoutManager(mLayoutManager);
        newsList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        newsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loadMore) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            page = page + 1;
                            newsPresenter.getListOfNews(String.valueOf(page));
                        }
                    }
                }
            }
        });
        allNewsBtn = findViewById(R.id.allNewsBtn);
        favBtn = findViewById(R.id.favBtn);
        allNewsBtn.setOnClickListener(this);
        favBtn.setOnClickListener(this);
        newsPresenter.getListOfNews(String.valueOf(page));
    }

    NewsContract.NewsPresenter newsPresenter;

    private void doInjection() {
        realmManager = RealmManager.getInstance(this);
        newsPresenter = new NewsPresenter(this, FactoryInjection.injectGetNewsUseCase(), this.realmManager);
    }

    ArticleDBModel articleDBModel = new ArticleDBModel();

    @Override
    public void onGetListOfNewsSuccess(NewsResponse newsResponse) {
        if (page == 1) {
            this.newsResponse = newsResponse;
            newsAdapter = new NewsAdapter(this,
                    newsResponse.getArticles(),
                    this,
                    this,
                    this.favList,
                    articleDBModel);
            newsList.setAdapter(newsAdapter);
        } else {
            this.newsResponse.getArticles().addAll(newsResponse.getArticles());
            if (newsResponse.getArticles().size() < limit)
                loadMore = false;
            newsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveItemSuccessful(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateCachedNewsList() {
        this.favList = newsPresenter.getCachedNews();
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveItemFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(NewsContract.NewsPresenter presenter) {
        this.newsPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean showIndicator) {
        if (showIndicator) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void onNewsItemClick(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onFavButtonClick(List<ArticleDBModel> cachedNews, ArticleDBModel article) {
        article = articleDBModel;
        newsPresenter.saveItem(this.favList, article);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allNewsBtn:
                //pass full list to adapter
                favPlaceholderTxt.setVisibility(View.GONE);
                newsList.setVisibility(View.VISIBLE);
                newsAdapter = new NewsAdapter(this,
                        this.newsResponse.getArticles(),
                        this,
                        this,
                        this.favList,
                        articleDBModel);
                newsList.setAdapter(newsAdapter);
                pageTitle.setText(getResources().getString(R.string.all_news));
                break;
            case R.id.favBtn:
                //pass fav list to adapter
                if (!realmManager.getRealm().isEmpty()) {
                    favPlaceholderTxt.setVisibility(View.GONE);
                    newsList.setVisibility(View.VISIBLE);
                    favList = newsPresenter.getCachedNews();
                    newsAdapter = new NewsAdapter(this,
                            this.favList,
                            this,
                            articleDBModel);
                    newsList.setAdapter(newsAdapter);
                } else {
                    newsList.setVisibility(View.GONE);
                    favPlaceholderTxt.setVisibility(View.VISIBLE);
                }
                pageTitle.setText(getResources().getString(R.string.favorites));
                break;
        }
    }
}
