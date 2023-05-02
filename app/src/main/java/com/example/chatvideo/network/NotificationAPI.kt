package com.example.chatvideo.network

import com.example.chatvideo.model.SendCalling
import com.example.chatvideo.util.Constant.API_KEY
import com.example.chatvideo.util.Constant.VALUE_TYPE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization:key=$API_KEY", "Content-Type:$VALUE_TYPE")
    @POST("fcm/send")
    suspend fun sendRemoteMessage(
        @Body notification: SendCalling
    ): Response<ResponseBody>

}