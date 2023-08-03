package com.yakov.cinema.ui.first_start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yakov.cinema.R
import com.yakov.cinema.databinding.FragmentFirstThreeBinding
import com.yakov.cinema.navigation.Navigator

class FirstThreeFragment : Fragment(R.layout.fragment_first_three) {

    lateinit var binding: FragmentFirstThreeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstThreeBinding.bind(view)

        binding.textViewSkipThree.setOnClickListener {
            (requireActivity() as Navigator).apply {
                navigateToPrimaryFragment()
                cleanSecondaryContainer()
            }
        }
    }

}