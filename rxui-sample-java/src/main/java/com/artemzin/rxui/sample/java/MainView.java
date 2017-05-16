package com.artemzin.rxui.sample.java;


import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

// Observable <-> Observable.
interface MainView {
    // Produces.
    Observable<String> login();
    Observable<String> password();
    Observable<Object> signInClicks();

    // Consumes.
    Function<Observable<Object>, Disposable> singInEnable();
    Function<Observable<Object>, Disposable> singInDisable();
    Function<Observable<AuthService.Success>, Disposable> signInSuccess();
    Function<Observable<AuthService.Failure>, Disposable> signInFailure();
}
