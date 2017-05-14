package com.artemzin.rxui.sample.kotlin

import com.artemzin.rxui.sample.kotlin.AuthService.Response
import io.reactivex.subjects.PublishSubject

open class TestAuthService : AuthService {

    val signIn = PublishSubject.create<Response>()

    override fun signIn(login: String, password: String) = signIn
}
