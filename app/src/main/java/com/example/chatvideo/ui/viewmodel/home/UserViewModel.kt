package com.example.chatvideo.ui.viewmodel.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.chatvideo.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {


    private val userRepository = UserRepository(application.applicationContext)

    val userLiveData = userRepository.getAllUserLiveData

    fun updateUserToken(context: Context) = userRepository.updateUserToken(context)
    fun updateUserStatus(context: Context, status: Boolean) =
        userRepository.updateUserStatus(context, status)

    fun getLogOut() = userRepository.logOut()

    fun getProfile(context: Context, onComplete: () -> Unit) =
        userRepository.signInRepository.getProfileData(
            context, FirebaseAuth.getInstance().currentUser!!.uid,
            onComplete
        )

    fun getAllUser() {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.getAllUser()
        }
    }

}