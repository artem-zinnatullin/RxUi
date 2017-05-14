package com.artemzin.rxui;

import android.os.Looper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.shadows.ShadowLooper;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(Robolectric.class)
public class RxUiTest {

    @Test
    public void bind() throws Exception {
        // WHEN have Observable and UI function
        Observable<String> observable = Observable.just("value");
        Function<Observable<String>, Disposable> uiFunc = mock(Function.class);

        Disposable expectedDisposable = mock(Disposable.class);
        when(uiFunc.apply(observable)).thenReturn(expectedDisposable);

        // AND bind Observable to UI function
        Disposable actualDisposable = RxUi.bind(observable, uiFunc);

        // THEN UI function should be called with passed Observable
        verify(uiFunc).apply(observable);

        // AND actual Disposable should be same as expected
        assertThat(actualDisposable).isSameAs(expectedDisposable);
    }

    @Test
    public void uiShouldNotCallActionUntilMainThreadIsPaused() throws Exception {
        // WHEN have UI action
        Consumer<String> uiAction = mock(Consumer.class);

        // AND produce binder func
        Function<Observable<String>, Disposable> binderFunc = RxUi.ui(uiAction);

        // AND main thread is paused
        ShadowLooper.pauseMainLooper();

        // AND bind Observable via binder func
        binderFunc.apply(Observable.just("a", "b", "c"));

        // THEN action should not be called until main thread is paused
        verifyZeroInteractions(uiAction);
    }

    @Test
    public void uiShouldCallAction() throws Exception {
        final AtomicBoolean wasCalledOnUiThread = new AtomicBoolean(true);

        // WHEN have UI action
        Consumer<String> uiAction = spy(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if (!wasCalledOnUiThread.compareAndSet(true, Looper.getMainLooper() == Looper.myLooper())) {
                    throw new IllegalStateException("Not on Main Thread!");
                }
            }
        });
        InOrder inOrder = inOrder(uiAction);

        // AND produce binder func
        Function<Observable<String>, Disposable> binderFunc = RxUi.ui(uiAction);

        // AND bind Observable via binder func
        binderFunc.apply(Observable.just("a", "b", "c"));

        // THEN action should be called with all values of source Observable on Main Thread
        inOrder.verify(uiAction).accept("a");
        inOrder.verify(uiAction).accept("b");
        inOrder.verify(uiAction).accept("c");
        inOrder.verifyNoMoreInteractions();
        assertThat(wasCalledOnUiThread.get()).isTrue();
    }

    @Test
    public void uiShouldPreventCallsToActionIfUnsubscribedBeforeExecution() throws Exception {
        // WHEN have UI action
        Consumer<String> uiAction = mock(Consumer.class);

        // AND produce binder func
        Function<Observable<String>, Disposable> binderFunc = RxUi.ui(uiAction);

        // AND pause Main Thread
        ShadowLooper.pauseMainLooper();

        // AND bind Observable via binder func
        Disposable disposable = binderFunc.apply(Observable.just("a", "b", "c"));

        // AND dispose Disposable
        disposable.dispose();

        // AND resume Main Thread
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        // THEN no calls to uiAction are expected
        verifyZeroInteractions(uiAction);
    }
}
