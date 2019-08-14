package com.example.pc.chatapp.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.pc.chatapp.R
import com.example.pc.chatapp.Services.AuthService
import com.example.pc.chatapp.Utilities.BROAD_CAST_USER_DATE_CHANGE
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.random.Random

class SignUpActivity : AppCompatActivity() {

    var userAvatar="profiledefault"
    var avatarColor=".5,.5,.5,1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        enableSpinner(false)
    }

    fun imgcreateAvatarClicked(view:View)
    {
        val avatar= Random.nextInt(28) // 0:27
        val color= Random.nextInt(2)
        if(color==1)
            userAvatar="light$avatar"
        else
            userAvatar="dark$avatar"
        imgUserSignUp.setImageResource(resources.getIdentifier(userAvatar,"drawable",packageName))
    }

    fun btnGenerateBackgroungColorClicked(view:View)
    {
        val r=Random.nextInt(255)
        val g=Random.nextInt(255)
        val b=Random.nextInt(255)
        avatarColor="$r,$g,$b,1"
        imgUserSignUp.setBackgroundColor(Color.rgb(r,g,b))

    }
    fun btncreateUserClicked(view: View)
    {
        enableSpinner(true)
        val email=txtEmailSignUp.text.toString()
        val password=txtPassSignUp.text.toString()
        val userName=txtNameSignUp.text.toString()
        if(userName.isNotEmpty()&& password.isNotEmpty()&& email.isNotEmpty()) {
            AuthService.register(this, email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.login(this, email, password)
                    { loginSuccess ->

                        if (loginSuccess) {
                            AuthService.createUser(this, userName, email, userAvatar, avatarColor) { createSuccess ->
                                if (createSuccess) {
                                    println("user created  ................................ $avatarColor")
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(BROAD_CAST_USER_DATE_CHANGE))
                                    enableSpinner(false)
                                    finish()
                                } else showError()

                            }
                        } else showError()
                    }
                } else showError()
            }
        }
        else
        {
            Toast.makeText(this,"please check name,email,password",Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }

    fun enableSpinner(enable:Boolean)
    {
        if(enable)
        {
            createSpinner.visibility=View.VISIBLE
            btnCreateUser.isEnabled=false
            btnBackgroundcolor.isEnabled=false
            imgUserSignUp.isEnabled=false
        }
        else
        {
            createSpinner.visibility=View.INVISIBLE
            btnCreateUser.isEnabled=true
            btnBackgroundcolor.isEnabled=true
            imgUserSignUp.isEnabled=true
        }
    }

    fun showError()
    {
        Toast.makeText(this,"please try again",Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }
}
