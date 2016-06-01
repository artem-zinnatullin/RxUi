package com.artemzin.rxui;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Basic functions for Reactive UI.
 *
 * @author Artem Zinnatullin.
 */
public class RxUi {

    /**
     * Binds some UI function to {@link Observable}. Usually used in Presenter/ViewModel/etc classes.
     *
     * @param observable not-null source {@link Observable}.
     * @param uiFunc     not-null function that actually performs binding of the {@link Observable} to something, for example UI.
     * @param <T>        type of {@link Observable} emission.
     * @return {@link Subscription} that can be used to unsubscribe and stop bound action.
     */
    public static <T> Subscription bind(Observable<T> observable, Func1<Observable<T>, Subscription> uiFunc) {
        return uiFunc.call(observable);
    }

    /**
     * Wraps passed UI action into function that binds {@link Observable} to UI action on Main Thread.
     *
     * @param uiAction action that performs some UI interaction on Main Thread, like setting text to {@link android.widget.TextView} and so on.
     * @param <T>      type of {@link Observable} emission.
     * @return {@link Func1} that can be used to {@link #bind(Observable, Func1)} {@link Observable} to some UI action.
     */
    public static <T> Func1<Observable<T>, Subscription> ui(final Action1<T> uiAction) {
        return new Func1<Observable<T>, Subscription>() {
            @Override
            public Subscription call(Observable<T> observable) {
                return observable
                        .observeOn(mainThread())
                        .subscribe(uiAction);
            }
        };
    }
}
