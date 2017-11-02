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
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

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
    public Observable<Object> signInClicks() {
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
    public Function<Observable<Object>, Disposable> signInEnable() {
        return RxUi.ui(enable -> signInButton.setEnabled(true));
    }

    @Override
    public Function<Observable<Object>, Disposable> signInDisable() {
        return RxUi.ui(disable -> signInButton.setEnabled(false));
    }

    @Override
    @SuppressLint("ShowToast") // Because of Retrolambda Lint gives false positive on this check.
    public Function<Observable<Success>, Disposable> signInSuccess() {
        return RxUi.ui(result -> Toast.makeText(signInButton.getContext(), "Success", LENGTH_SHORT).show());
    }

    @SuppressLint("ShowToast") // Because of Retrolambda Lint gives false positive on this check.
    @Override
    public Function<Observable<Failure>, Disposable> signInFailure() {
        return RxUi.ui(failure -> Toast.makeText(signInButton.getContext(), "Failure", LENGTH_SHORT).show());
    }
}
