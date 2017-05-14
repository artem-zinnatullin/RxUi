package com.artemzin.rxui.test;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Test function(s) for RxUi.
 *
 * @author Artem Zinnatullin.
 */
public class TestRxUi {

    /**
     * Wraps passed test UI action into function that synchronously binds {@link Observable} test UI action.
     *
     * @param testUiAction test action, usually {@code Mockito.mock(Action1.class)} that will be bound to the {@link Observable}.
     * @param <T>          type of {@link Observable} emission.
     * @return {@link Function} that can be used to {@link com.artemzin.rxui.RxUi#bind(Observable, Function)} {@link Observable} to some UI action.
     */
    public static <T> Function<Observable<T>, Disposable> testUi(final Consumer<T> testUiAction) {
        return new Function<Observable<T>, Disposable>() {
            @Override
            public Disposable apply(Observable<T> observable) {
                return observable.subscribe(testUiAction);
            }
        };
    }
}
