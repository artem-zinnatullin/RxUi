package com.artemzin.rxui.sample.kotlin

import com.artemzin.rxui.kotlin.bind
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Failure
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Success
import rx.Observable
import rx.Scheduler
import rx.Subscription
import rx.subscriptions.CompositeSubscription

class MainPresenter(private val authService: AuthService, private val ioScheduler: Scheduler) {

    fun bind(view: MainView): Subscription {
        val subscription = CompositeSubscription()

        val login = view.login.share()
        val password = view.password.share()

        // Boolean is valid/invalid flag.
        val credentials = Observable
                .combineLatest(login, password, { login, password -> Triple(login, password, login.isNotEmpty() && password.isNotEmpty()) })
                .share()

        subscription += credentials
                .filter { it.third }
                .map { Unit }
                .bind(view.signInEnable) // YES, that short, that simple and that readable!

        subscription += credentials
                .filter { !it.third }
                .map { Unit }
                .startWith(Unit) // Sign In should be disabled by default.
                .bind(view.signInDisable)

        val signInResult = view
                .signInClicks
                .withLatestFrom(credentials, { click, credentials -> credentials.first to credentials.second })
                .switchMap { authService.signIn(login = it.first, password = it.second).subscribeOn(ioScheduler) }
                .share()

        subscription += signInResult
                .filter { it is Success }
                .map { it as Success }
                .bind(view.signInSuccess)

        subscription += signInResult
                .filter { it is Failure }
                .map { it as Failure }
                .bind(view.signInFailure)

        return subscription
    }

    // Yeah, if you've read my blog, I was sceptical about operators, but if you'll use them carefully they'll help keep code readable.
    private operator fun CompositeSubscription.plusAssign(subscription: Subscription) = this.add(subscription)
}
