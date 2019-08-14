package com.example.pc.chatapp.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.example.pc.chatapp.Model.Channel
import com.example.pc.chatapp.R
import com.example.pc.chatapp.Services.AuthService
import com.example.pc.chatapp.Services.MessageService
import com.example.pc.chatapp.Services.UserDataService
import com.example.pc.chatapp.Utilities.BROAD_CAST_USER_DATE_CHANGE
import com.example.pc.chatapp.Utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket= IO.socket(SOCKET_URL)
    lateinit var channelAdapter:ArrayAdapter<Channel>
    private fun setupAdapter(){
        channelAdapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,MessageService.channels)
        lstChannels.adapter=channelAdapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        socket.connect()
        socket.on("channelCreated",onNewChannel)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        setupAdapter()

        if(App.sharedPrefs.isLoggedIn)
            AuthService.findUserByEmail(this){}
        else
            Log.d("perf","no cachhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh")

    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BROAD_CAST_USER_DATE_CHANGE))
        super.onResume()
    }



    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }

    private val userDataChangeReceiver=object: BroadcastReceiver()     //listenner
    {
        override fun onReceive(context: Context, inten: Intent?) {
            if(App.sharedPrefs.isLoggedIn)
            {
                txtEmailNavHeader.text=UserDataService.email
                txtNameNavHeader.text=UserDataService.name
                imgUserNavHeader.setImageResource(resources.getIdentifier(UserDataService.avatarName,"drawable",packageName))
                btnLoginNavHeader.text="LOGOUT"
                val r=UserDataService.avatarColor.split(',')[0].toInt()
                val g=UserDataService.avatarColor.split(',')[1].toInt()
                val b=UserDataService.avatarColor.split(',')[2].toInt()
                imgUserNavHeader.setBackgroundColor(Color.rgb(r,g,b))

                MessageService.getChannels(context){complete->
                    if(complete)
                    {
                        channelAdapter.notifyDataSetChanged()
                    }
                    else
                        println("no listtttttttttttttttt")
                }
            }
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun btnLoginNavClicked(view: View)
    {
        if(App.sharedPrefs.isLoggedIn)
        {
            UserDataService.logOut()
            //LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(BROAD_CAST_USER_DATE_CHANGE))
            txtNameNavHeader.text=""
            txtEmailNavHeader.text=""
            btnLoginNavHeader.text="LOGIN"
            imgUserNavHeader.setImageResource(R.drawable.profiledefault)
            imgUserNavHeader.setBackgroundColor(Color.TRANSPARENT)

        }
        else
        {

            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
    fun btnAddChannelNavClicked(view:View)
    {
        if(App.sharedPrefs.isLoggedIn)
        {
            val builder=AlertDialog.Builder(this)
            val dialogView=layoutInflater.inflate(R.layout.add_channel_dialog,null)
            builder.setView(dialogView).setPositiveButton("Add"){dialogInterface, i ->
                val channelName=dialogView.findViewById<EditText>(R.id.txtAddChannelName).text.toString()
                val channelDescription=dialogView.findViewById<EditText>(R.id.txtAddChannelDescription).text.toString()
                socket.emit("newChannel",channelName,channelDescription)
            }.setNegativeButton("cancle"){dialogInterface, i ->

            }.show()
        }
        else
            Toast.makeText(this,"please login ",Toast.LENGTH_LONG).show()
    }

    private val onNewChannel=Emitter.Listener { args ->
        runOnUiThread {
            val channelName=args[0] as String
            val channelDesc=args[1] as String
            val channelID=args[2] as String

            val newChannel=Channel(channelName,channelDesc,channelID)
            MessageService.channels.add(newChannel)
            channelAdapter.notifyDataSetChanged()
        }
    }

    fun btnSendMessageClicked(view:View)
    {

    }






}
