package com.artemzin.rxui.sample.java;


import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

// Observable <-> Observable.
interface MainView {
    // Produces.
    Observable<String> login();
    Observable<String> password();
    Observable<Void> signInClicks();

    // Consumes.
    Func1<Observable<Void>, Subscription> singInEnable();
    Func1<Observable<Void>, Subscription> singInDisable();
    Func1<Observable<AuthService.Success>, Subscription> signInSuccess();
    Func1<Observable<AuthService.Failure>, Subscription> signInFailure();
}
