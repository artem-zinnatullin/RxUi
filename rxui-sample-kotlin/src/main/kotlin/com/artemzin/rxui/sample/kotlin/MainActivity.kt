package com.artemzin.rxui.sample.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import rx.Subscription
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    lateinit var subscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val view = MainViewImpl(findViewById(android.R.id.content) as ViewGroup)
        val presenter = MainPresenter(authService = AuthService.Impl(), ioScheduler = Schedulers.io())

        subscription = presenter.bind(view)
    }

    override fun onDestroy() {
        subscription.unsubscribe()
        super.onDestroy()
    }
}
