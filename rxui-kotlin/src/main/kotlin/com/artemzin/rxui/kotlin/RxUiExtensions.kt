package com.artemzin.rxui.kotlin

import com.artemzin.rxui.RxUi
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function

/**
 * Binds some UI function to {@link Observable}. Usually used in Presenter/ViewModel/etc classes.
 *
 * @param uiFunc     not-null function that actually performs binding of the {@link Observable} to something, for example UI.
 * @param <T>        type of {@link Observable} emission.
 * @return {@link Disposable} that can be used to dispose and stop bound action.
 */
fun <T> Observable<T>.bind(uiFunc: Function<Observable<T>, Disposable>): Disposable = RxUi.bind(this, uiFunc)
