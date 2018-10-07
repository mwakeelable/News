package com.raye7.news.domain.base;


import com.raye7.news.presentation.base.BaseView;

import java.lang.ref.WeakReference;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseUseCaseObserver<P> extends DisposableObserver<P> {

  private WeakReference<BaseView> weakReference = null;

  public BaseUseCaseObserver(){
  }

  public BaseUseCaseObserver(BaseView view) {

    this.weakReference = new WeakReference<>(view);
  }

  protected abstract void onSuccess(P p);

  @Override
  public void onNext(P p) {
    onSuccess(p);
  }

  @Override
  public void onError(Throwable throwable) {

  }

  @Override
  public void onComplete() {
    if(isDisposed())
     dispose();
  }
}