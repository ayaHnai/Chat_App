package com.example.pc.chatapp.Services

import com.example.pc.chatapp.Controller.App


object UserDataService {

    var id=""
    var name=""
    var email=""
    var avatarName=""
    var avatarColor=""


    fun logout() {
        id = ""
        name = ""
        email = ""
        avatarName = ""
        avatarColor = ""
        App.sharedPrefs.authToken=""
        App.sharedPrefs.userEmail=""
        App.sharedPrefs.isLoggedIn=false
        MessageService.clearChannels()
        MessageService.clearChannels()


    }


}

