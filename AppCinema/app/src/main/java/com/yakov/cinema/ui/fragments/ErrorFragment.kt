package com.yakov.cinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.yakov.cinema.R
import com.yakov.cinema.databinding.FragmentErrorBinding
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.navigation.Navigator

class ErrorFragment : Fragment(R.layout.fragment_error) {

    private lateinit var binding: FragmentErrorBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentErrorBinding.bind(view)

        val back = arguments?.getString(BACK_FRAGMENT).toString()

        binding.buttonRefresh.setOnClickListener {
            (requireActivity() as Navigator).apply {
                cleanSecondaryContainer()
                when (back) {
                    Constants.BACK_HOME -> navigateToPrimaryFragment()
                    Constants.BACK_SEARCH -> navigateToSearchFragment()
                    Constants.BACK_PROFILE -> navigateToProfileFragment()
                    else -> navigateToErrorFragment(Constants.BACK_PROFILE)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    companion object {
        private const val BACK_FRAGMENT = "back_fragment"
        fun newInstance(back: String): ErrorFragment {
            val fragment = ErrorFragment()
            fragment.arguments = Bundle().apply {
                putString(BACK_FRAGMENT, back)
            }
            return fragment
        }
    }
}