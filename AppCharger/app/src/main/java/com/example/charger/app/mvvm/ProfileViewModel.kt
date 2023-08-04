package com.example.charger.app.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charger.data.model.user.AuthResponse
import com.example.charger.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(var userRepository: UserRepository) : ViewModel() {

    private var _userData = MutableLiveData<AuthResponse>()
    var userData: LiveData<AuthResponse> = _userData

    fun takeUserData() {
        viewModelScope.launch {
            val request = userRepository.getRequest()
            _userData.value = userRepository.getUserData(request)
        }
    }
}