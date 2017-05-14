package com.artemzin.rxui;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

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
     * @return {@link Disposable} that can be used to dispose and stop bound action.
     */
    public static <T> Disposable bind(Observable<T> observable, Function<Observable<T>, Disposable> uiFunc) {
        try {
            return uiFunc.apply(observable);
        } catch (Exception exception) {
            // Because of design error in RxJava v2 "Function" declares that it may throw checked exception.
            // We just wrap it with RuntimeException and throw further.
            throw new RuntimeException(exception);
        }
    }

    /**
     * Wraps passed UI action into function that binds {@link Observable} to UI action on Main Thread.
     *
     * @param uiAction action that performs some UI interaction on Main Thread, like setting text to {@link android.widget.TextView} and so on.
     * @param <T>      type of {@link Observable} emission.
     * @return {@link Function} that can be used to {@link #bind(Observable, Function)} {@link Observable} to some UI action.
     */
    public static <T> Function<Observable<T>, Disposable> ui(final Consumer<T> uiAction) {
        return new Function<Observable<T>, Disposable>() {
            @Override
            public Disposable apply(Observable<T> observable) {
                return observable
                        .observeOn(mainThread())
                        .subscribe(uiAction);
            }
        };
    }
}
