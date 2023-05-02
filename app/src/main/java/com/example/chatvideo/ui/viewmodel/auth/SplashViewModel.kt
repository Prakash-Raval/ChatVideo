package com.example.chatvideo.ui.viewmodel.auth
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*

class SplashViewModel(application: Application) : AndroidViewModel(application) {


    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            mutableLiveData.postValue(SplashState.MainActivity)
        }
    }


}

sealed class SplashState {
    object MainActivity : SplashState()
}
