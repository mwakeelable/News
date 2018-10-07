package com.raye7.news.domain.base;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public abstract class BaseUseCase<Q , P  > {

    private final CompositeDisposable compositeDisposable;

    public BaseUseCase() {
        compositeDisposable = new CompositeDisposable();
    }

    public void execute(Q request , Scheduler uiThread, Scheduler executorThread, BaseUseCaseObserver<P> disposableObserver) {

        if (disposableObserver == null) {
            throw new IllegalArgumentException("disposableObserver must not be null");
        }

         Observable<P> observable =
                         createObservableUseCase(request)
                        .observeOn(uiThread)
                        .subscribeOn(executorThread);

        DisposableObserver observer = observable.subscribeWith(disposableObserver);
        compositeDisposable.add(observer);
    }

    public void dispose() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    protected abstract Observable<P> createObservableUseCase(Q requestValues );


/*
    */
/**
     * Data passed to a request.
     *//*

    public interface RequestValues {
    }

    */
/**
     * Data received from a request.
     *//*

    public interface ResponseValue {
    }
*/


}
