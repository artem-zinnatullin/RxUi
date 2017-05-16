@file:Suppress("IllegalIdentifier")

package com.artemzin.rxui.kotlin

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.*

class RxUiExtensionsKtTest {

    // Yup, you can use whitespaces in function names in Kotlin: https://twitter.com/artem_zin/status/731151474017873920
    @Test
    fun `bind should bind ui function to Observable`() {
        // WHEN we have an Observable
        val observable = Observable.just("a", "b", "c")

        // AND we have a UI function
        val uiFunc = mock(Function::class.java) as Function<Observable<String>, Disposable>
        val expectedDisposable = mock(Disposable::class.java)
        `when`(uiFunc.apply(observable)).then { expectedDisposable }

        // AND we bind UI function to Observable
        val actualDisposable = observable.bind(uiFunc)

        // THEN UI function should be called with Observable
        verify(uiFunc).apply(observable)

        // THEN actual Disposable should be same as expected
        assertThat(actualDisposable).isSameAs(expectedDisposable)
    }
}
