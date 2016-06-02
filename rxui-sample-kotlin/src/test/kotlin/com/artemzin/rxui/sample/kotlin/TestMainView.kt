package com.artemzin.rxui.sample.kotlin

import com.artemzin.rxui.sample.kotlin.AuthService.Response.Failure
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Success
import com.artemzin.rxui.test.TestRxUi.testUi
import org.mockito.Mockito
import rx.functions.Action1
import rx.subjects.PublishSubject

class TestMainView : MainView {

    val signInEnableAction = Mockito.mock(Action1::class.java) as Action1<Unit>
    val signInDisableAction = Mockito.mock(Action1::class.java) as Action1<Unit>
    val signInSuccessAction = Mockito.mock(Action1::class.java) as Action1<Success>
    val signInFailureAction = Mockito.mock(Action1::class.java) as Action1<Failure>

    // Produces.
    override val login = PublishSubject.create<String>()
    override val password = PublishSubject.create<String>()
    override val signInClicks = PublishSubject.create<Unit>()

    // Consumes.
    override val signInEnable = testUi { enable: Unit -> signInEnableAction.call(Unit) }
    override val signInDisable = testUi { disable: Unit -> signInDisableAction.call(Unit) }
    override val signInSuccess = testUi { success: Success -> signInSuccessAction.call(success) }
    override val signInFailure = testUi { failure: Failure -> signInFailureAction.call(failure) }
}
