package com.example.chatvideo.service

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chatvideo.model.NotificationData
import com.example.chatvideo.util.Constant.INVITATION_RESPONSE
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class MassagingService : FirebaseMessagingService() {


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val dataJson = Gson().toJson(message.data)
        val data = Gson().fromJson(dataJson.toString(), NotificationData::class.java)

        val intents = Intent(INVITATION_RESPONSE)
        intents.putExtra("data", data)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intents)

    }

}