package com.raye7.news.presentation.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.raye7.news.data.db.ArticleDBModel;
import com.raye7.news.presentation.home.NewsAdapter.NewsViewHolder;

import com.raye7.news.R;
import com.raye7.news.data.network.news.models.Article;
import com.raye7.news.utils.UIManager;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private Context mContext;
    private List<Article> articles;
    private List<ArticleDBModel> articleDBModels;
    private List<ArticleDBModel> cachedNews;
    private NewsClickInteractionListener newsClickInteractionListener;
    private FavButtonClickListener favButtonClickListener;
    ArticleDBModel articleDBModel;

    interface NewsClickInteractionListener {
        void onNewsItemClick(String url);
    }

    interface FavButtonClickListener {
        void onFavButtonClick(List<ArticleDBModel> cachedNews, ArticleDBModel article);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsName, newsTime;
        ImageView newsImage;
        RelativeLayout news_item;
        ImageButton favBtn;

        NewsViewHolder(View view) {
            super(view);
            newsName = view.findViewById(R.id.news_name);
            newsTime = view.findViewById(R.id.news_time);
            newsImage = view.findViewById(R.id.news_image);
            news_item = view.findViewById(R.id.news_item);
            favBtn = view.findViewById(R.id.favBtn);
        }
    }

    public NewsAdapter(Context mContext, List<Article> articles,
                       NewsClickInteractionListener newsClickInteractionListener,
                       FavButtonClickListener favButtonClickListener,
                       List<ArticleDBModel> articleDBModels,
                       ArticleDBModel articleDBModel) {
        this.mContext = mContext;
        this.articles = articles;
        this.newsClickInteractionListener = newsClickInteractionListener;
        this.favButtonClickListener = favButtonClickListener;
        this.cachedNews = articleDBModels;
        this.articleDBModel = articleDBModel;
    }

    public NewsAdapter(Context context, List<ArticleDBModel> articleDBModels,
                       NewsClickInteractionListener mNewsClickInteractionListener,
                       ArticleDBModel articleDBModel) {
        this.mContext = context;
        this.articleDBModels = articleDBModels;
        this.newsClickInteractionListener = mNewsClickInteractionListener;
        this.articleDBModel = articleDBModel;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, final int i) {
        if (articleDBModels == null) {
            final Article article = this.articles.get(i);
            prepareData(newsViewHolder,
                    article.getTitle(),
                    article.getPublishedAt(),
                    article.getUrlToImage(),
                    article.getUrl());
            //favBtn Handling
            newsViewHolder.favBtn.setVisibility(View.VISIBLE);
            newsViewHolder.favBtn.setTag(article.getUrl());
            newsViewHolder.favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    articleDBModel.setUrl(article.getUrl());
                    articleDBModel.setUrlToImage(article.getUrlToImage());
                    articleDBModel.setPublishedAt(article.getPublishedAt());
                    articleDBModel.setTitle(article.getTitle());
                    favButtonClickListener.onFavButtonClick(cachedNews, articleDBModel);
                }
            });
        } else {
            final ArticleDBModel articleDBModel = this.articleDBModels.get(i);
            prepareData(newsViewHolder,
                    articleDBModel.getTitle(),
                    articleDBModel.getPublishedAt(),
                    articleDBModel.getUrlToImage(),
                    articleDBModel.getUrl());
            newsViewHolder.favBtn.setVisibility(View.GONE);
        }
    }

    private void prepareData(NewsViewHolder newsViewHolder,
                             String title, String date, String imgURL, final String url) {
        //set name and time
        newsViewHolder.newsName.setText(title);
        newsViewHolder.newsTime.setText(UIManager.getDateFormat(date));
        //Prepare Glide to load image
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.news_image_placeholder);
        requestOptions.error(R.drawable.news_image_placeholder);
        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(imgURL).into(newsViewHolder.newsImage);
        //onClick Handling
        newsViewHolder.news_item.setTag(url);
        newsViewHolder.news_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsClickInteractionListener.onNewsItemClick(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.articleDBModels == null)
            return this.articles.size();
        else
            return this.articleDBModels.size();
    }
}
