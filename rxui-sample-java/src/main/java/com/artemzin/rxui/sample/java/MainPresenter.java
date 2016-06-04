package com.artemzin.rxui.sample.java;

import android.support.annotation.NonNull;

import com.artemzin.rxui.RxUi;
import com.artemzin.rxui.sample.java.AuthService.Failure;
import com.artemzin.rxui.sample.java.AuthService.Success;

import org.javatuples.Triplet;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;

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
    Subscription bind(MainView view) {
        final CompositeSubscription subscription = new CompositeSubscription();

        Observable<String> login = view.login().share();
        Observable<String> password = view.password().share();

        // Boolean is valid/invalid flag.
        ConnectableObservable<Triplet<String, String, Boolean>> credentials = Observable
                .combineLatest(login, password, (l, p) -> Triplet.with(l, p, !l.isEmpty() && !p.isEmpty()))
                .publish();

        Observable<Void> signInEnable = credentials
                .filter(creds -> creds.getValue2())
                .map(enable -> null);

        Observable<Void> signInDisable = credentials
                .filter(creds -> !creds.getValue2())
                .map(disable -> null);

        // You can use static import for RxUi.bind()
        subscription.add(RxUi.bind(signInEnable, view.singInEnable()));
        subscription.add(RxUi.bind(signInDisable, view.singInDisable()));

        Observable<Object> signInResult = view
                .signInClicks()
                .withLatestFrom(credentials, (click, creds) -> creds.removeFrom2()) // Leave only login and password.
                .switchMap(loginAndPassword -> authService.signIn(loginAndPassword.getValue0(), loginAndPassword.getValue1()).subscribeOn(ioScheduler)) // "API request".
                .share();

        credentials.connect();

        Observable<Success> signInSuccess = signInResult
                .filter(it -> it instanceof Success)
                .cast(Success.class);

        Observable<Failure> signInFailure = signInResult
                .filter(it -> it instanceof Failure)
                .cast(Failure.class);

        // You can use static import for RxUi.bind()
        subscription.add(RxUi.bind(signInSuccess, view.signInSuccess()));
        subscription.add(RxUi.bind(signInFailure, view.signInFailure()));

        return subscription;
    }
}
