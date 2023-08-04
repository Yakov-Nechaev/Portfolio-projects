package com.example.charger.app.navigator

import com.example.charger.app.model.Coordinates

interface Navigator {

    fun goToRouteMapActivity(coordinates: Coordinates)

    fun goToPhoneCallActivity(phone: String?)

    fun goToShareActivity(coordinates: Coordinates, title: String?)

    fun navigateToMapFragment()

    fun navigateToDescriptionFragment(id: String)

    fun navigateToAuthorizationFragment()

    fun navigateToAccountFragment()

    fun navigateToCompleteDescriptionFragment(id: String)

    fun goBack()

}