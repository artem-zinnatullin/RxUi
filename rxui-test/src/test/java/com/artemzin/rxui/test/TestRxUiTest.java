package com.artemzin.rxui.test;

import org.junit.Test;
import org.mockito.InOrder;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class TestRxUiTest {

    @Test
    public void testUiShouldCallActionSynchronously() throws Exception {
        // WHEN have some test UI action
        Consumer<String> testUiAction = mock(Consumer.class);
        InOrder inOrder = inOrder(testUiAction);

        // AND produce binder func
        Function<Observable<String>, Disposable> binderFunc = TestRxUi.testUi(testUiAction);

        // AND bind Observable via test binder func
        binderFunc.apply(Observable.just("a", "b", "c"));

        // THEN should call action with all values in required order
        inOrder.verify(testUiAction).accept("a");
        inOrder.verify(testUiAction).accept("b");
        inOrder.verify(testUiAction).accept("c");
        inOrder.verifyNoMoreInteractions();
    }
}
