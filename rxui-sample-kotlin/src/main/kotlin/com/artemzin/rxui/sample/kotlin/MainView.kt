package com.artemzin.rxui.sample.kotlin

import com.artemzin.rxui.sample.kotlin.AuthService.Response.Failure
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Success
import rx.Observable
import rx.Subscription
import rx.functions.Func1

// Observable <-> Observable.
interface MainView {
    // Produces.
    val login: Observable<String>
    val password: Observable<String>
    val signInClicks: Observable<Unit>

    // Consumes.
    val signInEnable: Func1<Observable<Unit>, Subscription>
    val signInDisable: Func1<Observable<Unit>, Subscription>
    val signInSuccess: Func1<Observable<Success>, Subscription>
    val signInFailure: Func1<Observable<Failure>, Subscription>
}
