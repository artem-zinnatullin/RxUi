package com.artemzin.rxui.sample.java;


import android.support.annotation.NonNull;

import rx.Observable;

interface AuthService {

    class Success {

    }

    class Failure {

    }

    @NonNull
    Observable<Object> signIn(@NonNull String login, @NonNull String password);

    class Impl implements AuthService {

        @NonNull
        @Override
        public Observable<Object> signIn(@NonNull String login, @NonNull String password) {
            return Observable.fromCallable(() -> (login.equals("@artem_zin") && password.equals("rxui") ? new Success() : new Failure()));
        }
    }
}
