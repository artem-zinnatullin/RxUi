package com.artemzin.rxui.sample.java;

import android.support.annotation.NonNull;

import com.artemzin.rxui.RxUi;
import com.artemzin.rxui.sample.java.AuthService.Failure;
import com.artemzin.rxui.sample.java.AuthService.Success;

import org.javatuples.Triplet;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

// Since most of you use MVP, sample app uses MVP too.
class MainPresenter {

    @NonNull
    private final AuthService authService;

    @NonNull
    private final Scheduler ioScheduler;

    // As you can see, we don't need Main Thread scheduler anymore!
    MainPresenter(@NonNull AuthService authService, @NonNull Scheduler ioScheduler) {
        this.authService = authService;
        this.ioScheduler = ioScheduler;
    }

    @NonNull
    Disposable bind(MainView view) {
        final CompositeDisposable disposable = new CompositeDisposable();

        Observable<String> login = view.login().share();
        Observable<String> password = view.password().share();

        // Boolean is valid/invalid flag.
        ConnectableObservable<Triplet<String, String, Boolean>> credentials = Observable
                .combineLatest(login, password, (l, p) -> Triplet.with(l, p, !l.isEmpty() && !p.isEmpty()))
                .publish();

        Observable<Object> signInEnable = credentials
                .filter(creds -> creds.getValue2())
                .map(enable -> new Object());

        Observable<Object> signInDisable = credentials
                .filter(creds -> !creds.getValue2())
                .map(disable -> new Object());

        // You can use static import for RxUi.bind()
        disposable.add(RxUi.bind(signInEnable, view.signInEnable()));
        disposable.add(RxUi.bind(signInDisable, view.signInDisable()));

        Observable<Object> signInResult = view
                .signInClicks()
                .withLatestFrom(credentials, (click, creds) -> creds.removeFrom2()) // Leave only login and password.
                .switchMap(loginAndPassword -> authService
                        .signIn(loginAndPassword.getValue0(), loginAndPassword.getValue1())
                        .subscribeOn(ioScheduler)) // "API request".
                .share();

        disposable.add(credentials.connect());

        Observable<Success> signInSuccess = signInResult
                .filter(it -> it instanceof Success)
                .cast(Success.class);

        Observable<Failure> signInFailure = signInResult
                .filter(it -> it instanceof Failure)
                .cast(Failure.class);

        // You can use static import for RxUi.bind()
        disposable.add(RxUi.bind(signInSuccess, view.signInSuccess()));
        disposable.add(RxUi.bind(signInFailure, view.signInFailure()));

        return disposable;
    }
}
