package com.example.chatvideo.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatvideo.model.User
import com.example.chatvideo.util.Constant.SIGNIN
import com.example.chatvideo.util.Constant.USERS
import com.example.chatvideo.util.Constant.USER_PROFILE
import com.example.chatvideo.util.Constant.editor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInRepository {

    private val sigInLiveData = MutableLiveData<Boolean>()
    fun getSignIn(): LiveData<Boolean> = sigInLiveData


    fun signIn(context: Context, email: String, password: String) =
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    editor(context).putBoolean(SIGNIN, true).apply()
                    getProfileData(context, FirebaseAuth.getInstance().currentUser!!.uid) {
                        sigInLiveData.postValue(true)
                    }
                } else {
                    sigInLiveData.postValue(false)
                }
            }

    fun getProfileData(context: Context, uid: String, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseFirestore.getInstance().collection(USERS)
                .document(uid)
                .addSnapshotListener { querySnapshot, _ ->
                    val userString = Gson().toJson(querySnapshot!!.toObject(User::class.java))
                    editor(context)!!.putString(USER_PROFILE, userString).apply()
                }
            delay(1500)
            onComplete()
        }
    }


}