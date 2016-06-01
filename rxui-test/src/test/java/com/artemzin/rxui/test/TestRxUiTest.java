package com.artemzin.rxui.test;

import org.junit.Test;
import org.mockito.InOrder;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class TestRxUiTest {

    @Test
    public void testUiShouldCallActionSynchronously() {
        // WHEN have some test UI action
        Action1<String> testUiAction = mock(Action1.class);
        InOrder inOrder = inOrder(testUiAction);

        // AND produce binder func
        Func1<Observable<String>, Subscription> binderFunc = TestRxUi.testUi(testUiAction);

        // AND bind Observable via test binder func
        binderFunc.call(Observable.from(asList("a", "b", "c")));

        // THEN should call action with all values in required order
        inOrder.verify(testUiAction).call("a");
        inOrder.verify(testUiAction).call("b");
        inOrder.verify(testUiAction).call("c");
        inOrder.verifyNoMoreInteractions();
    }
}
