package com.example.chatvideo.ui.viewmodel.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.chatvideo.model.User
import com.example.chatvideo.repository.SignUpRepository


class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val signUpRepository = SignUpRepository()

    fun createAccount(user: User) = signUpRepository.createNewAccount(user)

    fun getSignUp() = signUpRepository.getSignUp()

}