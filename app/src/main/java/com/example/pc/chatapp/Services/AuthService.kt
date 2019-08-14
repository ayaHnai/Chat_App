package com.example.pc.chatapp.Services

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.android.volley.toolbox.Volley
import com.example.pc.chatapp.Controller.App
import com.example.pc.chatapp.Utilities.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method

object AuthService {

    /*var isLogged=false
    var userEmail=""
    var authToken=""*/

    fun register(context:Context,email:String,password:String,complete:(Boolean)->Unit)
    {
        val url=URL_REGISTER
        val jsonBody=JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        val requestBody=jsonBody.toString()

        val registerRequest=object: StringRequest(
            Request.Method.POST, url, Response.Listener { _ ->
                Log.d("success","aaa")
                complete(true)}
             , Response.ErrorListener { error ->
                Log.d("error", "coudnt register $error")
                complete(false)})
        {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }//req return string

        Volley.newRequestQueue(context).add(registerRequest)

    }

    fun login(context: Context, email: String, password: String,complete: (Boolean) -> Unit)
    {
        val url= URL_LOgin
        val jsonBody=JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        val requestBody=jsonBody.toString()

        val loginRequest=object : JsonObjectRequest(Method.POST,url,null,Response.Listener {response ->
            try
            {
                App.sharedPrefs.userEmail=response.getString("user")
                App.sharedPrefs.authToken=response.getString("token")
                App.sharedPrefs.isLoggedIn=true
                complete(true)
            }catch (e:JSONException){
                Log.d("json exception : ", e.localizedMessage)
                complete(false)
            }
        },Response.ErrorListener { error ->
            Log.d("error", "couldnt login $error")
            complete(false)
        })
        {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        } //return json responce

        Volley.newRequestQueue(context).add(loginRequest)

    }

    fun createUser(context: Context,name: String,email: String,avatarName:String,avatarColor:String, complete: (Boolean) -> Unit)
    {
        val jsonBody=JSONObject()
        jsonBody.put("name",name)
        jsonBody.put("email",email)
        jsonBody.put("avatarName",avatarName)
        jsonBody.put("avatarColor",avatarColor)
        val requestBody=jsonBody.toString()

        val createRequest=object : JsonObjectRequest(Method.POST, URL_CREATEUSER,null,Response.Listener { response ->
        try
        {
            UserDataService.avatarColor=response.getString("avatarColor")
            UserDataService.avatarName=response.getString("avatarName")
            UserDataService.email=response.getString("email")
            UserDataService.id=response.getString("_id")
            UserDataService.name=response.getString("name")
            complete(true)
        }catch (e:JSONException){
            Log.d("json exception : ", e.localizedMessage)
            println("id bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
            println(response.getString("id"))
            complete(false)
        }
    },Response.ErrorListener { error ->
        Log.d("error", "couldnt create user  $error")
        complete(false)
    })
    {

        override fun getBodyContentType(): String {
            return "application/json; charset=utf-8"
        }

        override fun getBody(): ByteArray {
            return requestBody.toByteArray()
        }

        override fun getHeaders(): MutableMap<String, String> {
            val headers=HashMap<String,String>()
            headers.put("Authorization","Bearer ${App.sharedPrefs.authToken}")
            println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn $App.sharedPrefs.authToken")
            return headers
        }
    } //return json responce

        App.sharedPrefs.requestQueue.add(createRequest)
    }

    fun findUserByEmail(context: Context,complete:(Boolean)->Unit)
    {
        val findUserRequest=object : JsonObjectRequest(Method.GET,"$URL_GET_USER${App.sharedPrefs.userEmail}",null,Response.Listener {response->
            try {
                UserDataService.avatarColor=response.getString("avatarColor")
                UserDataService.avatarName=response.getString("avatarName")
                UserDataService.email=response.getString("email")
                UserDataService.id=response.getString("_id")
                UserDataService.name=response.getString("name")


                complete(true)
            }catch (e:JSONException)
            {
                Log.d("json exeption","${e.localizedMessage}")
            }
        },Response.ErrorListener {error->
            Log.d("error","Could not find the user by email ${App.sharedPrefs.userEmail}")
            complete(false)
        })
        {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers=HashMap<String,String>()
                headers.put("Authorization","Bearer ${App.sharedPrefs.authToken}")
                println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn ${App.sharedPrefs.authToken}")
                return headers
            }
        }
        App.sharedPrefs.requestQueue.add(findUserRequest)
    }
}