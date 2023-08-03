package com.yakov.cinema.navigation

interface Navigator {

    fun navigateToPrimaryFragment()
    fun navigateToSearchFragment()
    fun navigateToSearchParamsFragment()
    fun navigateToDetailsFragment(id: Int)
    fun navigateToPersonnelFragment(id: Int)
    fun navigateToProfileFragment()
    fun showLoadingFragment()
    fun dismissLoadingFragment()
    fun navigateToShowAllFragment(type: String)
    fun navigateToShowAllPersonnelFragment(type: String, id: Int)
    fun navigateToShowAllPhotoFragment(id: Int)
    fun cleanSecondaryContainer()
    fun showWelcome()
    fun navigateToErrorFragment(back: String)
    fun goBack()

}