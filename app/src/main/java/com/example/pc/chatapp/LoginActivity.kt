package com.example.pc.chatapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun btnLoginClicked(view: View)
    {

    }

    fun btnSignUpClicked(view: View)
    {
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent)
    }

}
