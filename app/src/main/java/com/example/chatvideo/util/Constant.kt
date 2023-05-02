package com.example.chatvideo.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.chatvideo.R
import com.google.gson.Gson
import com.example.chatvideo.model.User


object Constant {

    const val SHARE = "Share"
    const val SIGNIN = "signIn"
    const val USERS = "User"
    const val USER_PROFILE = "userProfile"
    const val USER_DATA = "userData"
    const val TYPE_CALL = "typeCall"
    const val TYPE = "type"
    const val CALL_VIDEO = "video"
    const val CALL_AUDIO = "audio"
    const val VALUE_TYPE = "application/json"
    const val REMOTE_MSG_INVITATION = "invitation"

    const val INVITATION_RESPONSE = "invitationResponse"
    const val INVITATION_ACCEPTED = "accepted"
    const val INVITATION_REJECTED = "rejected"
    const val INVITATION_CANCEL = "cancel"
    const val API_KEY =
        "AAAANGBtoyA:APA91bEElycvoRBlcuClV4UUZANf4ffbVREPlK8zdo9EOEPtikoKaqjN6OsBuE4Moe8MZp1cA1IR7Udocx3Hctau4TP02nrMzuw2T-F1w0Lzj5N0gi3b0lHbksr6N--SP0_HErB0kJ41"
    const val TOKEN = "token"
    const val BaseUrl = "https://fcm.googleapis.com/"
    const val MEETURL = "https://meet.jit.si"
    const val MEETING_ROOM = "meetingRoom"
    const val USER_STATUS = "status"

    lateinit var ringtone: Ringtone

    fun getSharePref(context: Context) =
        context.getSharedPreferences(SHARE, Activity.MODE_PRIVATE)

    fun editor(context: Context) = getSharePref(context).edit()


    lateinit var dialog: Dialog
    fun showDialog(activity: Activity) {
        dialog = Dialog(activity).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(true)
        }
        dialog.show()
    }

    fun getUserProfile(context: Context): User =
        Gson().fromJson(getSharePref(context)!!.getString(USER_PROFILE, ""), User::class.java)


    @RequiresApi(Build.VERSION_CODES.P)
    fun playerRingtone(context: Context) {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context, notification)
        ringtone.play()
        ringtone.isLooping = true
    }

    fun stopRingtone() {
        ringtone.stop()
    }

}