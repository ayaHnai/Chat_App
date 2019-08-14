package com.example.pc.chatapp.Controller

import android.app.Application
import com.example.pc.chatapp.Utilities.SharedPrefs

class App:Application() {

    companion object{
        lateinit var sharedPrefs:SharedPrefs
    }

    override fun onCreate() {
        sharedPrefs=SharedPrefs(applicationContext)
        super.onCreate()
    }

}