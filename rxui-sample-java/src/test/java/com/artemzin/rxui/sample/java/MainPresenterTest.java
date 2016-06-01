package com.artemzin.rxui.sample.java;

import android.support.annotation.NonNull;

import com.artemzin.rxui.sample.java.AuthService.Failure;
import com.artemzin.rxui.sample.java.AuthService.Success;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static com.artemzin.rxui.test.TestRxUi.testUi;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MainPresenterTest {

    static class TestAuthService implements AuthService {

        final PublishSubject<Object> signIn = PublishSubject.create();

        @NonNull
        @Override
        public Observable<Object> signIn(@NonNull String login, @NonNull String password) {
            return signIn;
        }
    }

    static class TestMainView implements MainView {

        final PublishSubject<String> login = PublishSubject.create();
        final PublishSubject<String> password = PublishSubject.create();
        final PublishSubject<Void> signInClicks = PublishSubject.create();

        Action1<Void> signInEnable = mock(Action1.class);
        Action1<Void> signInDisable = mock(Action1.class);
        Action1<Success> signInSuccess = mock(Action1.class);
        Action1<Failure> signInFailure = mock(Action1.class);

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
        public Func1<Observable<Success>, Subscription> signInSuccess() {
            return testUi(signInSuccess);
        }

        @Override
        public Func1<Observable<Failure>, Subscription> signInFailure() {
            return testUi(signInFailure);
        }
    }

    TestAuthService authService = spy(new TestAuthService());
    Scheduler ioScheduler = Schedulers.immediate();
    TestMainView view = new TestMainView();

    MainPresenter mainPresenter = new MainPresenter(authService, ioScheduler);

    @Before
    public void beforeEachTest() {
        mainPresenter.bind(view);

    }

    @Test
    public void shouldEnableSignInIfBothLoginAndPasswordAreNotEmpty() {
        // WHEN both login is not empty
        view.login.onNext("a");

        // AND password is not empty
        view.password.onNext("1");

        // THEN sign in should be enabled
        verify(view.signInEnable).call(null);
        verifyZeroInteractions(view.signInDisable);
    }

    @Test
    public void shouldNotEnableSignInIfLoginIsNotEmptyButPasswordIsEmpty() {
        // WHEN login is not empty
        view.login.onNext("a");

        // AND password is empty
        view.password.onNext("");

        // THEN sign in should be disabled
        verify(view.signInDisable).call(null);
        verifyZeroInteractions(view.signInEnable);
    }

    @Test
    public void shouldNotEnableSignInIfLoginIsEmptyButPasswordIsNotEmpty() {
        // WHEN login is empty
        view.login.onNext("");

        // AND password is not empty
        view.password.onNext("a");

        // THEN sign in should be disabled
        verify(view.signInDisable).call(null);
        verifyZeroInteractions(view.signInEnable);
    }

    @Test
    public void shouldSendRequestToAuthService() {
        // WHEN login is not empty (typing simulation)
        view.login.onNext("@art");
        view.login.onNext("@artem_zin");

        // AND password is not empty (typing simulation)
        view.password.onNext("123");
        view.password.onNext("123456");

        // AND click on sign in happens
        view.signInClicks.onNext(null);

        // THEN should call signIn service with correct credentials (not intermediate ones)
        verify(authService).signIn("@artem_zin", "123456");
        verifyNoMoreInteractions(authService);
    }

    @Test
    public void shouldSendSuccessSignInResultToView() {
        // WHEN login is not empty
        view.login.onNext("abc");

        // AND password is not empty
        view.password.onNext("213");

        // AND click on sign in happens
        view.signInClicks.onNext(null);

        // AND signIn response arrives
        Success success = new Success();
        authService.signIn.onNext(success);

        // THEN should send signIn result to view
        verify(view.signInSuccess).call(success);
        verifyZeroInteractions(view.signInFailure);
    }

    @Test
    public void shouldSendFailureSignInResultToView() {
        // WHEN login is not empty
        view.login.onNext("abc");

        // AND password is not empty
        view.password.onNext("213");

        // AND click on sign in happens
        view.signInClicks.onNext(null);

        // AND signIn response arrives
        Failure failure = new Failure();
        authService.signIn.onNext(failure);

        // THEN should send signIn result to view
        verify(view.signInFailure).call(failure);
        verifyZeroInteractions(view.signInSuccess);
    }
}
