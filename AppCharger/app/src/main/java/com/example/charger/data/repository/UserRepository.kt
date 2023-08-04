package com.example.charger.data.repository

import com.example.charger.data.AuthInterceptor
import com.example.charger.data.NetworkService
import com.example.charger.data.StoreManager
import com.example.charger.data.model.user.AuthRequest
import com.example.charger.data.model.user.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(var storeManager: StoreManager, val networkService: NetworkService) {

    suspend fun getUserToken(authRequest: AuthRequest): String? {
        return try {
            val response = networkService.service.authenticate(request = authRequest)
            response.userData.accessToken
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRequest() : AuthRequest {
        return storeManager.getAuthRequest()
    }

    suspend fun getUserData(request: AuthRequest): AuthResponse {
        return withContext(Dispatchers.IO) {
            networkService.service.authenticate(request = request)
        }
    }

    suspend fun saveRequest(authRequest: AuthRequest) {
        storeManager.saveAuthRequest(authRequest)
    }

    fun updateNetworkServiceWithAccessToken(accessToken: String) {
        val interceptor = AuthInterceptor(accessToken)
        networkService.setAuthInterceptor(interceptor)
    }
}