package com.example.charger.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.charger.app.App
import com.example.charger.data.model.user.AuthRequest
import kotlinx.coroutines.flow.first

class StoreManager(val context: Context) {

    suspend fun saveAuthRequest(authRequest: AuthRequest) {
        (context as App).dataStore.edit { pref ->
            pref[loginKey] = authRequest.emailaddress
            pref[passwordKey] = authRequest.password
        }
    }

    suspend fun getAuthRequest(): AuthRequest {
        val login = (context as App).dataStore.data.first()[loginKey] ?: ""
        val password = context.dataStore.data.first()[passwordKey] ?:""
        return AuthRequest(emailaddress = login, password = password)
    }

    companion object {
        private const val KEY_LOGIN = "login"
        private const val KEY_PASSWORD = "password"
        private val loginKey = stringPreferencesKey(KEY_LOGIN)
        private val passwordKey = stringPreferencesKey(KEY_PASSWORD)
    }
}