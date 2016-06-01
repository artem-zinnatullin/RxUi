package com.artemzin.rxui.test;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

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
     * @return {@link Func1} that can be used to {@link com.artemzin.rxui.RxUi#bind(Observable, Func1)} {@link Observable} to some UI action.
     */
    public static <T> Func1<Observable<T>, Subscription> testUi(final Action1<T> testUiAction) {
        return new Func1<Observable<T>, Subscription>() {
            @Override
            public Subscription call(Observable<T> observable) {
                return observable.subscribe(testUiAction);
            }
        };
    }
}
