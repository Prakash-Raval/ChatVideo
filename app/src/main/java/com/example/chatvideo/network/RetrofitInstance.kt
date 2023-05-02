package com.example.chatvideo.network

import android.content.Context
import com.example.chatvideo.util.Constant.BaseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance private constructor(context: Context) {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var notificationApi: NotificationAPI = retrofit.create(NotificationAPI::class.java)

    companion object {
        @Volatile
        private var instance: RetrofitInstance? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) =
            instance ?: synchronized(LOCK) {
                instance ?: createPreferences(context).also {
                    instance = it
                }
            }

        private fun createPreferences(context: Context) =
            RetrofitInstance(context)

    }

}