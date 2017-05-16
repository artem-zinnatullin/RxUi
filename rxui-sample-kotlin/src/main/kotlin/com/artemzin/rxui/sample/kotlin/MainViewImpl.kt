package com.artemzin.rxui.sample.kotlin

import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.artemzin.rxui.RxUi.ui
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Failure
import com.artemzin.rxui.sample.kotlin.AuthService.Response.Success
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges

class MainViewImpl(private val content: ViewGroup) : MainView {

    private val loginEditText = content.findViewById(R.id.main_login_edit_text) as EditText
    private val passwordEditText = content.findViewById(R.id.main_password_edit_text) as EditText
    private val signInButton = content.findViewById(R.id.main_sign_in_button) as Button

    // Produces.
    override val login = loginEditText.textChanges().map { it.toString() }
    override val password = passwordEditText.textChanges().map { it.toString() }
    override val signInClicks = signInButton.clicks()

    // Consumes.
    override val signInEnable = ui<Unit> { signInButton.isEnabled = true }
    override val signInDisable = ui<Unit> { signInButton.isEnabled = false }
    override val signInSuccess = ui<Success> { Toast.makeText(content.context, "Success", LENGTH_SHORT).show() }
    override val signInFailure = ui<Failure> { Toast.makeText(content.context, "Failure", LENGTH_SHORT).show() }
}
