package com.raye7.news.presentation.base;


public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

    void setLoadingIndicator(boolean b);

    boolean isActive();


}
