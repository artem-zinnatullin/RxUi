package com.artemzin.rxui.sample.kotlin

import com.artemzin.rxui.sample.kotlin.AuthService.Response.Failure
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Success
import com.artemzin.rxui.test.TestRxUi.testUi
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.mockito.Mockito

class TestMainView : MainView {

    val signInEnableAction = Mockito.mock(Consumer::class.java) as Consumer<Unit>
    val signInDisableAction = Mockito.mock(Consumer::class.java) as Consumer<Unit>
    val signInSuccessAction = Mockito.mock(Consumer::class.java) as Consumer<Success>
    val signInFailureAction = Mockito.mock(Consumer::class.java) as Consumer<Failure>

    // Produces.
    override val login = PublishSubject.create<String>()
    override val password = PublishSubject.create<String>()
    override val signInClicks = PublishSubject.create<Unit>()

    // Consumes.
    override val signInEnable = testUi { enable: Unit -> signInEnableAction.accept(Unit) }
    override val signInDisable = testUi { disable: Unit -> signInDisableAction.accept(Unit) }
    override val signInSuccess = testUi { success: Success -> signInSuccessAction.accept(success) }
    override val signInFailure = testUi { failure: Failure -> signInFailureAction.accept(failure) }
}
