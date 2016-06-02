package com.artemzin.rxui.sample.java;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.subjects.PublishSubject;

class TestAuthService implements AuthService {

    final PublishSubject<Object> signIn = PublishSubject.create();

    @NonNull
    @Override
    public Observable<Object> signIn(@NonNull String login, @NonNull String password) {
        return signIn;
    }
}
