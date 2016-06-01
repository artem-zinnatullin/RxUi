package com.artemzin.rxui.kotlin

import com.artemzin.rxui.RxUi
import rx.Observable
import rx.Subscription
import rx.functions.Func1

/**
 * Binds some UI function to {@link Observable}. Usually used in Presenter/ViewModel/etc classes.
 *
 * @param uiFunc     not-null function that actually performs binding of the {@link Observable} to something, for example UI.
 * @param <T>        type of {@link Observable} emission.
 * @return {@link Subscription} that can be used to unsubscribe and stop bound action.
 */
fun <T> Observable<T>.bind(uiFunc: Func1<Observable<T>, Subscription>): Subscription = RxUi.bind(this, uiFunc)
