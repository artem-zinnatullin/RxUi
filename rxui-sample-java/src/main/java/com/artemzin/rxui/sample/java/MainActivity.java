package com.artemzin.rxui.sample.java;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import rx.Subscription;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MainView mainView = new MainViewImpl((ViewGroup) findViewById(android.R.id.content));
        MainPresenter mainPresenter = new MainPresenter(new AuthService.Impl(), Schedulers.io());

        subscription = mainPresenter.bind(mainView);
    }

    @Override
    protected void onDestroy() {
        subscription.unsubscribe();
        super.onDestroy();
    }
}
