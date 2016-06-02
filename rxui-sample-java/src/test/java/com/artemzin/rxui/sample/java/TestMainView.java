package com.artemzin.rxui.sample.java;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

import static com.artemzin.rxui.test.TestRxUi.testUi;
import static org.mockito.Mockito.mock;

class TestMainView implements MainView {

    final PublishSubject<String> login = PublishSubject.create();
    final PublishSubject<String> password = PublishSubject.create();
    final PublishSubject<Void> signInClicks = PublishSubject.create();

    Action1<Void> signInEnable = mock(Action1.class);
    Action1<Void> signInDisable = mock(Action1.class);
    Action1<AuthService.Success> signInSuccess = mock(Action1.class);
    Action1<AuthService.Failure> signInFailure = mock(Action1.class);

    @Override
    public Observable<String> login() {
        return login;
    }

    @Override
    public Observable<String> password() {
        return password;
    }

    @Override
    public Observable<Void> signInClicks() {
        return signInClicks;
    }

    @Override
    public Func1<Observable<Void>, Subscription> singInEnable() {
        return testUi(signInEnable); // No need for imitation of Main Thread scheduler!
    }

    @Override
    public Func1<Observable<Void>, Subscription> singInDisable() {
        return testUi(signInDisable);
    }

    @Override
    public Func1<Observable<AuthService.Success>, Subscription> signInSuccess() {
        return testUi(signInSuccess);
    }

    @Override
    public Func1<Observable<AuthService.Failure>, Subscription> signInFailure() {
        return testUi(signInFailure);
    }
}
