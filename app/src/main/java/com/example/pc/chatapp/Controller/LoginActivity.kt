package com.example.pc.chatapp.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.pc.chatapp.R
import com.example.pc.chatapp.Services.AuthService
import com.example.pc.chatapp.Utilities.BROAD_CAST_USER_DATE_CHANGE
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        enableSpinner(false)
    }

    fun btnLoginClicked(view: View)
    {
        enableSpinner(true)
        val email=txtEmailLogin.text.toString()
        val pass=txtPassLogin.text.toString()
        if(email.isNotEmpty()&&pass.isNotEmpty()) {
            AuthService.login(this, email, pass) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(BROAD_CAST_USER_DATE_CHANGE))
                            enableSpinner(false)
                            finish()
                        } else showError()
                    }
                } else showError()
            }
        }
        else
            Toast.makeText(this,"please check email, password",Toast.LENGTH_LONG).show()
    }

    fun btnSignUpClicked(view: View)
    {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        //finish()
    }

    fun enableSpinner(enable:Boolean)
    {
        if(enable)
            loginSpinner.visibility=View.VISIBLE
        else
            loginSpinner.visibility=View.INVISIBLE
        btnLogin.isEnabled=!enable
        btnSignUp.isEnabled=!enable
    }

    fun showError()
    {
        Toast.makeText(this,"please try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }



}
