package com.example.charger.app.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charger.data.model.user.AuthRequest
import com.example.charger.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthorizationViewModel(val userRepository: UserRepository) : ViewModel() {

    private var _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    fun takeToken(authRequest: AuthRequest) {
        viewModelScope.launch {
            val accessToken = userRepository.getUserToken(authRequest)
            accessToken?.let { userRepository.updateNetworkServiceWithAccessToken(it) }
            _token.value = userRepository.getUserToken(authRequest)
        }
    }

    fun saveRequest(authRequest: AuthRequest) {
        viewModelScope.launch {
            userRepository.saveRequest(authRequest)
        }
    }
}