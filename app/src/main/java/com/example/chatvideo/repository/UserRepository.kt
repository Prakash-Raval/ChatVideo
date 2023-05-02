package com.example.chatvideo.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.chatvideo.model.User
import com.example.chatvideo.util.Constant.TOKEN
import com.example.chatvideo.util.Constant.USERS
import com.example.chatvideo.util.Constant.USER_STATUS
import com.example.chatvideo.util.Constant.getUserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class UserRepository constructor(val context: Context) {

    private val updateLiveData = MutableLiveData<Boolean>()
    val signInRepository = SignInRepository()
    val getAllUserLiveData = MutableLiveData<List<User>>()


    private fun updateData(data: Map<String, Any>, id: String, onComplete: () -> Unit) = FirebaseFirestore
        .getInstance()
        .collection(USERS)
        .document(id)
        .update(
            data
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                updateLiveData.postValue(true)
                onComplete()
            } else {
                updateLiveData.postValue(false)
            }
        }

    fun updateUserToken(context: Context) {
        val user = getUserProfile(context)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                updateData(mapOf(TOKEN to it.result.toString()), user.id) {
                    signInRepository.getProfileData(context, user.id) {
                    }
                }
            }
        }
    }

    fun updateUserStatus(context: Context, status: Boolean) {
        val user = getUserProfile(context)
        updateData(mapOf(USER_STATUS to status), user.id) {
            signInRepository.getProfileData(context, user.id) {
            }
        }
    }

    fun logOut() {
        val user = getUserProfile(context)
        updateData(mapOf(TOKEN to " "), user.id) {
            FirebaseAuth.getInstance().signOut()
        }
    }


    fun getAllUser() {
        val array = ArrayList<User>()
        FirebaseFirestore.getInstance().collection(USERS).addSnapshotListener { querySnapshot, _ ->
            array.clear()
            querySnapshot?.documents!!.forEach {
                val item = it.toObject(User::class.java)
                if (item!!.id != FirebaseAuth.getInstance().currentUser!!.uid)
                    array.add(item)
            }
            getAllUserLiveData.value = array
        }
    }


}