@file:Suppress("IllegalIdentifier")

package com.artemzin.rxui.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.*
import rx.Observable
import rx.Subscription
import rx.functions.Func1

class RxUiExtensionsKtTest {

    // Yup, you can use whitespaces in function names in Kotlin: https://twitter.com/artem_zin/status/731151474017873920
    @Test
    fun `bind should bind ui function to Observable`() {
        // WHEN we have an Observable
        val observable = Observable.from(listOf("a", "b", "c"))

        // AND we have a UI function
        val uiFunc = mock(Func1::class.java) as Func1<Observable<String>, Subscription>
        val expectedSubscription = mock(Subscription::class.java)
        `when`(uiFunc.call(observable)).then { expectedSubscription }

        // AND we bind UI function to Observable
        val actualSubscription = observable.bind(uiFunc)

        // THEN UI function should be called with Observable
        verify(uiFunc).call(observable)

        // THEN actual subscription should be same as expected
        assertThat(actualSubscription).isSameAs(expectedSubscription)
    }
}
