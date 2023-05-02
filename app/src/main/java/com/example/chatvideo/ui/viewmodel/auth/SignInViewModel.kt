package com.example.chatvideo.ui.viewmodel.auth
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.chatvideo.repository.SignInRepository

class SignInViewModel(application: Application) : AndroidViewModel(application) {


    private val sigInRepository = SignInRepository()

    fun signInWithEmailAndPassword(context: Context,email: String, password: String) =
        sigInRepository.signIn(context,email,password)

    fun getSignIn() = sigInRepository.getSignIn()


}