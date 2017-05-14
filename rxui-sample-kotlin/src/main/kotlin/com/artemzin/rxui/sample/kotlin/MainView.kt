package com.artemzin.rxui.sample.kotlin

import com.artemzin.rxui.sample.kotlin.AuthService.Response.Failure
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Success
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function

// Observable <-> Observable.
interface MainView {
    // Produces.
    val login: Observable<String>
    val password: Observable<String>
    val signInClicks: Observable<Unit>

    // Consumes.
    val signInEnable: Function<Observable<Unit>, Disposable>
    val signInDisable: Function<Observable<Unit>, Disposable>
    val signInSuccess: Function<Observable<Success>, Disposable>
    val signInFailure: Function<Observable<Failure>, Disposable>
}
