package com.example.pc.chatapp.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pc.chatapp.Controller.App
import com.example.pc.chatapp.Model.Channel
import com.example.pc.chatapp.Utilities.URL_GET_CHANNELS
import org.json.JSONException

object MessageService {
    val channels=ArrayList<Channel>()

    fun getChannels(context: Context,complete:(Boolean)->Unit)
    {
        val channelsRequest=object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS,null,Response.Listener { response ->
            try {
                for(x in 0 until response.length())
                {
                    val channel=response.getJSONObject(x)
                    val name=channel.getString("name")
                    val desc=channel.getString("description")
                    val id=channel.getString("_id")

                    val channelAdd=Channel(name,desc,id)
                    channels.add(channelAdd)

                }

            }catch (e:JSONException)
            {
                Log.d("EXC",e.localizedMessage)
            }
        },Response.ErrorListener {error->
            Log.d("error ","coudn't get channels ")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers=HashMap<String,String>()
                headers.put("Authorization","Bearer ${App.sharedPrefs.authToken}")
               // println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn ${AuthService.authToken}")
                return headers
            }
        }
        App.sharedPrefs.requestQueue.add(channelsRequest)
    }
}