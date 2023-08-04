package com.example.charger.di

import com.example.charger.app.mvvm.AuthorizationViewModel
import com.example.charger.app.mvvm.DescriptionViewModel
import com.example.charger.app.mvvm.MapViewModel
import com.example.charger.app.mvvm.ProfileViewModel
import com.example.charger.data.NetworkService
import com.example.charger.data.StoreManager
import com.example.charger.data.repository.MapRepository
import com.example.charger.data.repository.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val presentationModule = module {
    factory<MapViewModel> { MapViewModel(mapRepository = get()) }
    factory<AuthorizationViewModel> { AuthorizationViewModel(userRepository = get()) }
    factory<ProfileViewModel> { ProfileViewModel(userRepository = get()) }
    factory<DescriptionViewModel> { DescriptionViewModel(mapRepository = get()) }
}

val dataModule = module {
    factory <NetworkService> { NetworkService() }
    factory <NetworkService.OcmApiService> { get<NetworkService>().service }
    factory<MapRepository> { MapRepository(ocmApiService = get()) }
    factory<UserRepository> { UserRepository(storeManager = get(), networkService = get()) }
    factory<StoreManager> { StoreManager(androidContext()) }
}