package com.artemzin.rxui.sample.kotlin

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction

class Observables private constructor() {
    companion object {
        inline fun <T1, T2> combineLatest(o1: Observable<T1>, o2: Observable<T2>): Observable<Pair<T1, T2>> =
                Observable.combineLatest(o1, o2, BiFunction { t1, t2 -> t1 to t2 })
    }
}

inline fun <T1, T2, R> Observable<T1>.withLatestFrom(observable: Observable<T2>, crossinline combiner: (T1, T2) -> R): Observable<R> =
        withLatestFrom(observable, BiFunction { t1, t2 -> combiner.invoke(t1, t2) })

// Yeah, if you've read my blog, I was sceptical about operators, but if you'll use them carefully they'll help keep code readable.
inline operator fun CompositeDisposable.plusAssign(disposable: Disposable) = this.add(disposable).let { Unit }
