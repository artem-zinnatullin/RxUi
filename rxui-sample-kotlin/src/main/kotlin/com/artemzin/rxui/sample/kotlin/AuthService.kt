package com.artemzin.rxui.sample.kotlin

import com.artemzin.rxui.sample.kotlin.AuthService.Response.Failure
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Success
import rx.Observable

interface AuthService {

    // Kind of algebraic data type, compiler will check that we handle all cases and so on.
    sealed class Response {
        object Success : Response()
        class Failure(val cause: Throwable) : Response()
    }

    fun signIn(login: String, password: String): Observable<Response>

    class Impl : AuthService {
        override fun signIn(login: String, password: String) = Observable.fromCallable {
            if (login == "@artem_zin" && password == "rxui") Success
            else Failure(IllegalArgumentException("Incorrect credentials"))
        }
    }
}
