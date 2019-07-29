package com.example.pc.chatapp

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.random.Random

class SignUpActivity : AppCompatActivity() {

    var userAvatar="profiledefault"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
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
        imgUserSignUp.setBackgroundColor(Color.rgb(r,g,b))

    }
    fun btncreateUserClicked(view: View)
    {

    }
}
