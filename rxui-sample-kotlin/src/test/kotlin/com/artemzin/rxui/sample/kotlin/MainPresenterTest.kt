@file:Suppress("IllegalIdentifier")

package com.artemzin.rxui.sample.kotlin

import com.artemzin.rxui.sample.kotlin.AuthService.Response.Failure
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Success
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import rx.schedulers.Schedulers

class MainPresenterTest {

    val authService = spy(TestAuthService())
    val ioScheduler = Schedulers.immediate()
    val view = TestMainView()

    val presenter = MainPresenter(authService, ioScheduler)

    @Before
    fun beforeEachTest() {
        // GIVEN presenter is bound to view
        presenter.bind(view)

        // THEN sign in should be disabled by default
        verify(view.signInDisableAction).call(Unit)
        verifyZeroInteractions(view.signInEnableAction)

        // Reset counter for tests.
        reset(view.signInDisableAction)
    }

    @Test
    fun `should enable sign in if both login and password are not empty`() {
        // GIVEN login is not empty
        view.login.onNext("a")

        // AND password is not empty
        view.password.onNext("1")

        // THEN sign in should be enabled
        verify(view.signInEnableAction).call(Unit)
        verifyZeroInteractions(view.signInDisableAction)
    }

    @Test
    fun `should not enable sign in if login is not empty but password is empty`() {
        // WHEN login is not empty
        view.login.onNext("a")

        // AND password is empty
        view.password.onNext("")

        // THEN sign in should be disabled
        verify(view.signInDisableAction).call(Unit)
        verifyZeroInteractions(view.signInEnableAction)
    }

    @Test
    fun `should not enable sign in if login is empty but password is not empty`() {
        // WHEN login is empty
        view.login.onNext("")

        // AND password is not empty
        view.password.onNext("a")

        // THEN sign in should be disabled
        verify(view.signInDisableAction).call(Unit)
        verifyZeroInteractions(view.signInEnableAction)
    }

    @Test
    fun `should send request to auth service`() {
        // WHEN login is not empty (typing simulation)
        view.login.onNext("@art")
        view.login.onNext("@artem_zin")

        // AND password is not empty (typing simulation)
        view.password.onNext("123")
        view.password.onNext("123456")

        // AND click on sign in happens
        view.signInClicks.onNext(null)

        // THEN should call signIn service with correct credentials (not intermediate ones)
        verify(authService).signIn("@artem_zin", "123456")
        verifyNoMoreInteractions(authService)
    }

    @Test
    fun `should send success sign in result to view`() {
        // WHEN login is not empty
        view.login.onNext("abc")

        // AND password is not empty
        view.password.onNext("213")

        // AND click on sign in happens
        view.signInClicks.onNext(Unit)

        // AND signIn response arrives
        authService.signIn.onNext(Success)

        // THEN should send signIn result to view
        verify(view.signInSuccessAction).call(Success)
        verifyZeroInteractions(view.signInFailureAction)
    }

    @Test
    fun `should send failure sign in result to view`() {
        // WHEN login is not empty
        view.login.onNext("abc")

        // AND password is not empty
        view.password.onNext("213")

        // AND click on sign in happens
        view.signInClicks.onNext(Unit)

        // AND signIn response arrives
        val failure = Failure(RuntimeException("test"))
        authService.signIn.onNext(failure)

        // THEN should send signIn result to view
        verify(view.signInFailureAction).call(failure)
        verifyZeroInteractions(view.signInSuccessAction)
    }
}
