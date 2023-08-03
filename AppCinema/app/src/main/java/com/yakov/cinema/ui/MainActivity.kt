package com.yakov.cinema.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.yakov.cinema.R
import com.yakov.cinema.databinding.ActivityMainBinding
import com.yakov.cinema.ui.first_start.PagerFirstStartFragment
import com.yakov.cinema.navigation.Navigator
import com.yakov.cinema.ui.fragments.DetailsFragment
import com.yakov.cinema.ui.fragments.ErrorFragment
import com.yakov.cinema.ui.fragments.LoadingFragment
import com.yakov.cinema.ui.fragments.PersonnelFragment
import com.yakov.cinema.ui.fragments.PrimaryFragment
import com.yakov.cinema.ui.fragments.ProfileFragment
import com.yakov.cinema.ui.fragments.SearchFragment
import com.yakov.cinema.ui.fragments.ShowAllFragment
import com.yakov.cinema.ui.fragments.ShowAllPersonnelFragment
import com.yakov.cinema.ui.fragments.ShowAllPhotoFragment
import com.yakov.cinema.ui.view_models.MainViewModel
import com.yakov.cinema.ui.view_models.MainFactory

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> { MainFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) viewModel.apply {
            fillDataApp()
            fillWelcome()
        }

        viewModel.welcome.observe(this) { welcome ->
            if (savedInstanceState == null) {
                when (welcome) {
                    null -> {
                        showWelcome()
                        viewModel.saveWithoutWelcome()
                    }

                    true -> showWelcome()
                    false -> navigateToPrimaryFragment()
                }
            } else navigateToPrimaryFragment()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> navigateToPrimaryFragment()
                R.id.search -> navigateToSearchFragment()
                R.id.profile -> navigateToProfileFragment()
            }
            true
        }
    }

    override fun navigateToErrorFragment(back: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.secondaryContainerView, ErrorFragment.newInstance(back))
            .setReorderingAllowed(true)
            .commit()
    }

    override fun showWelcome() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.secondaryContainerView, PagerFirstStartFragment())
            .setReorderingAllowed(true)
            .commit()
    }

    override fun showLoadingFragment() {
        val dialogFragment = LoadingFragment()
        dialogFragment.show(supportFragmentManager, "loading")
    }

    override fun dismissLoadingFragment() {
        val dialogFragment = supportFragmentManager.findFragmentByTag("loading") as? LoadingFragment
        dialogFragment?.dismiss()
    }

    override fun cleanSecondaryContainer() {
        if (supportFragmentManager.findFragmentById(R.id.secondaryContainerView) != null) {
            supportFragmentManager
                .beginTransaction()
                .remove(supportFragmentManager.findFragmentById(R.id.secondaryContainerView)!!)
                .commit()
        }
    }

    override fun navigateToShowAllFragment(type: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, ShowAllFragment.newInstance(type))
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToShowAllPersonnelFragment(type: String, id: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, ShowAllPersonnelFragment.newInstance(type, id))
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToShowAllPhotoFragment(id: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, ShowAllPhotoFragment.newInstance(id))
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToProfileFragment() {
        supportFragmentManager.popBackStack(null, 1)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, ProfileFragment())
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToPrimaryFragment() {
        supportFragmentManager.popBackStack(null, 1)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, PrimaryFragment())
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToSearchFragment() {
        supportFragmentManager.popBackStack(null, 1)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, SearchFragment())
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToSearchParamsFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, SearchFragment.newInstance("params"))
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToDetailsFragment(id: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, DetailsFragment.newInstance(id))
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToPersonnelFragment(id: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, PersonnelFragment.newInstance(id))
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun goBack() {
        supportFragmentManager.popBackStack()
    }

}