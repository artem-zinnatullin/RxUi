package com.artemzin.rxui;

import android.os.Looper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.shadows.ShadowLooper;

import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(Robolectric.class)
public class RxUiTest {

    @Test
    public void bind() {
        // WHEN have Observable and UI function
        Observable<String> observable = Observable.just("value");
        Func1<Observable<String>, Subscription> uiFunc = mock(Func1.class);

        Subscription expectedSubscription = mock(Subscription.class);
        when(uiFunc.call(observable)).thenReturn(expectedSubscription);

        // AND bind Observable to UI function
        Subscription actualSubscription = RxUi.bind(observable, uiFunc);

        // THEN UI function should be called with passed Observable
        verify(uiFunc).call(observable);

        // AND actual subscription should be same as expected
        assertSame(actualSubscription, expectedSubscription);

        // Robolectric does not have some JDK8 classes assertThat(actualSubscription).isSameAs(expectedSubscription);
    }

    @Test
    public void uiShouldNotCallActionUntilMainThreadIsPaused() {
        // WHEN have UI action
        Action1<String> uiAction = mock(Action1.class);

        // AND produce binder func
        Func1<Observable<String>, Subscription> binderFunc = RxUi.ui(uiAction);

        // AND main thread is paused
        ShadowLooper.pauseMainLooper();

        // AND bind Observable via binder func
        binderFunc.call(Observable.from(asList("a", "b", "c")));

        // THEN action should not be called until main thread is paused
        verifyZeroInteractions(uiAction);
    }

    @Test
    public void uiShouldCallAction() {
        final AtomicBoolean wasCalledOnUiThread = new AtomicBoolean(true);

        // WHEN have UI action
        Action1<String> uiAction = spy(new Action1<String>() {
            @Override
            public void call(String s) {
                if (!wasCalledOnUiThread.compareAndSet(true, Looper.getMainLooper() == Looper.myLooper())) {
                    throw new IllegalStateException("Not on Main Thread!");
                }
            }
        });
        InOrder inOrder = inOrder(uiAction);

        // AND produce binder func
        Func1<Observable<String>, Subscription> binderFunc = RxUi.ui(uiAction);

        // AND bind Observable via binder func
        binderFunc.call(Observable.from(asList("a", "b", "c")));

        // THEN action should be called with all values of source Observable on Main Thread
        inOrder.verify(uiAction).call("a");
        inOrder.verify(uiAction).call("b");
        inOrder.verify(uiAction).call("c");
        inOrder.verifyNoMoreInteractions();
        assertTrue(wasCalledOnUiThread.get());
    }

    @Test
    public void uiShouldPreventCallsToActionIfUnsubscribedBeforeExecution() {
        // WHEN have UI action
        Action1<String> uiAction = mock(Action1.class);

        // AND produce binder func
        Func1<Observable<String>, Subscription> binderFunc = RxUi.ui(uiAction);

        // AND pause Main Thread
        ShadowLooper.pauseMainLooper();

        // AND bind Observable via binder func
        Subscription subscription = binderFunc.call(Observable.from(asList("a", "b", "c")));

        // AND unsubscribe from subscription
        subscription.unsubscribe();

        // AND resume Main Thread
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        // THEN no calls to uiAction are expected
        verifyZeroInteractions(uiAction);
    }
}
