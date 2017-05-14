package com.artemzin.rxui.sample.kotlin

import com.artemzin.rxui.kotlin.bind
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Failure
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Success
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainPresenter(private val authService: AuthService, private val ioScheduler: Scheduler) {

    fun bind(view: MainView): Disposable {
        val disposable = CompositeDisposable()

        val login = view.login.share()
        val password = view.password.share()


        // Boolean is valid/invalid flag.
        fun Pair<String, String>.valid(): Boolean {
            val (login, password) = this
            return login.isNotEmpty() && password.isNotEmpty()
        }

        val credentials = Observables
                .combineLatest(login, password)
                .publish()

        disposable += credentials
                .filter { it.valid() }
                .map { Unit }
                .bind(view.signInEnable) // YES, that short, that simple and that readable!

        disposable += credentials
                .filter { !it.valid() }
                .map { Unit }
                .startWith(Unit) // Sign In should be disabled by default.
                .bind(view.signInDisable)

        val signInResult = view
                .signInClicks
                .withLatestFrom(credentials, { click, credentials -> credentials.first to credentials.second })
                .switchMap { authService.signIn(login = it.first, password = it.second).subscribeOn(ioScheduler) }
                .share()

        disposable += credentials.connect()

        disposable += signInResult
                .filter { it is Success }
                .map { it as Success }
                .bind(view.signInSuccess)

        disposable += signInResult
                .filter { it is Failure }
                .map { it as Failure }
                .bind(view.signInFailure)

        return disposable
    }
}
