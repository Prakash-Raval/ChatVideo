package com.example.chatvideo.ui.viewmodel.call
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.chatvideo.model.NotificationData
import com.example.chatvideo.model.SendCalling
import com.example.chatvideo.util.Constant
import com.example.chatvideo.util.Constant.INVITATION_ACCEPTED
import com.example.chatvideo.util.Constant.INVITATION_CANCEL
import com.example.chatvideo.util.Constant.REMOTE_MSG_INVITATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

class CallViewModel(application: Application) : AndroidViewModel(application) {

    val callLivedata = MutableLiveData<Boolean>()

    fun startCalling(context: Context,meetingRoom:String,audio:Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL(Constant.MEETURL))
                .setRoom(meetingRoom)
                .setVideoMuted(audio)
                .setAudioMuted(true)
                .setFeatureFlag("prejoinpage.enabled", false)
                .setFeatureFlag("chat.enabled", false)
                .setFeatureFlag("invite.enabled", false)
                .build()
            JitsiMeetActivity.launch(context, options)
        }
    }

     fun sendRemoteMessage(context: Context, notificationData: NotificationData, type: String, messageTo:String){
         CoroutineScope(Dispatchers.IO).launch  {
             SendCalling(
                 notificationData,
                 messageTo
             ).also {
                 SendCalling().Notification().sendMessage(context, it) { msg, done ->
                     if (done) {
                         try {
                             when(type){
                                 INVITATION_ACCEPTED,REMOTE_MSG_INVITATION,INVITATION_CANCEL-> callLivedata.postValue(true)
                                 else-> callLivedata.postValue(false)
                             }

                         } catch (e: Exception) {
                             e.printStackTrace()
                             callLivedata.postValue(false)
                         }
                     } else {
                         callLivedata.postValue(false)
                         Log.e("tttttttttttt", msg!!)
                     }
                 }
             }
         }
    }



}