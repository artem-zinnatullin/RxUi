package com.artemzin.rxui.sample.java;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.artemzin.rxui.RxUi;
import com.artemzin.rxui.sample.java.AuthService.Failure;
import com.artemzin.rxui.sample.java.AuthService.Success;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

import static android.widget.Toast.LENGTH_SHORT;

class MainViewImpl implements MainView {

    @NonNull
    private final EditText loginEditText, passwordEditText;

    @NonNull
    private final Button signInButton;

    MainViewImpl(@NonNull ViewGroup content) {
        loginEditText = (EditText) content.findViewById(R.id.main_login_edit_text);
        passwordEditText = (EditText) content.findViewById(R.id.main_password_edit_text);
        signInButton = (Button) content.findViewById(R.id.main_sign_in_button);
    }

    @Override
    public Observable<Void> signInClicks() {
        return RxView.clicks(signInButton);
    }

    @Override
    public Observable<String> login() {
        return RxTextView.textChanges(loginEditText).map(CharSequence::toString);
    }

    @Override
    public Observable<String> password() {
        return RxTextView.textChanges(passwordEditText).map(CharSequence::toString);
    }

    @Override
    public Func1<Observable<Void>, Subscription> singInEnable() {
        return RxUi.ui(enable -> signInButton.setEnabled(true));
    }

    @Override
    public Func1<Observable<Void>, Subscription> singInDisable() {
        return RxUi.ui(disable -> signInButton.setEnabled(false));
    }

    @Override
    @SuppressLint("ShowToast") // Because of Retrolambda Lint gives false positive on this check.
    public Func1<Observable<Success>, Subscription> signInSuccess() {
        return RxUi.ui(result -> Toast.makeText(signInButton.getContext(), "Success", LENGTH_SHORT).show());
    }

    @SuppressLint("ShowToast") // Because of Retrolambda Lint gives false positive on this check.
    @Override
    public Func1<Observable<Failure>, Subscription> signInFailure() {
        return RxUi.ui(failure -> Toast.makeText(signInButton.getContext(), "Failure", LENGTH_SHORT).show());
    }
}
