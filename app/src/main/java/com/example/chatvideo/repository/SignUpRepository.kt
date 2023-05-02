package com.example.chatvideo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatvideo.model.User
import com.example.chatvideo.util.Constant.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignUpRepository {

    private val sigUpLiveData = MutableLiveData<Boolean>()


    fun createNewAccount(user: User) =
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    sendEmailVerification(user)
                } else {
                    sigUpLiveData.postValue(false)
                }
            }

    fun getSignUp(): LiveData<Boolean> = sigUpLiveData

    private fun sendEmailVerification(user: User) {
        val userAuth = FirebaseAuth.getInstance().currentUser
        userAuth!!.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                insertUser(
                    User(
                        FirebaseAuth.getInstance().uid.toString(),
                        user.name,
                        user.email,
                        user.password,
                        user.image
                    )
                )
            }
        }
    }

    private fun insertUser(user: User) = FirebaseFirestore
        .getInstance()
        .collection(USERS)
        .document(user.id).set(user)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                sigUpLiveData.postValue(true)
            } else {
                sigUpLiveData.postValue(false)
            }
        }
}