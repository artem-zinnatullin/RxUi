package com.artemzin.rxui.sample.java;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MainView mainView = new MainViewImpl((ViewGroup) findViewById(android.R.id.content));
        MainPresenter mainPresenter = new MainPresenter(new AuthService.Impl(), Schedulers.io());

        disposable = mainPresenter.bind(mainView);
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
