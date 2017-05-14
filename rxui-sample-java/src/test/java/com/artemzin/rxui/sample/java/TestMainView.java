package com.artemzin.rxui.sample.java;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

import static com.artemzin.rxui.test.TestRxUi.testUi;
import static org.mockito.Mockito.mock;

class TestMainView implements MainView {

    final PublishSubject<String> login = PublishSubject.create();
    final PublishSubject<String> password = PublishSubject.create();
    final PublishSubject<Object> signInClicks = PublishSubject.create();

    Consumer<Object> signInEnable = mock(Consumer.class);
    Consumer<Object> signInDisable = mock(Consumer.class);
    Consumer<AuthService.Success> signInSuccess = mock(Consumer.class);
    Consumer<AuthService.Failure> signInFailure = mock(Consumer.class);

    @Override
    public Observable<String> login() {
        return login;
    }

    @Override
    public Observable<String> password() {
        return password;
    }

    @Override
    public Observable<Object> signInClicks() {
        return signInClicks;
    }

    @Override
    public Function<Observable<Object>, Disposable> singInEnable() {
        return testUi(signInEnable); // No need for imitation of Main Thread scheduler!
    }

    @Override
    public Function<Observable<Object>, Disposable> singInDisable() {
        return testUi(signInDisable);
    }

    @Override
    public Function<Observable<AuthService.Success>, Disposable> signInSuccess() {
        return testUi(signInSuccess);
    }

    @Override
    public Function<Observable<AuthService.Failure>, Disposable> signInFailure() {
        return testUi(signInFailure);
    }
}
