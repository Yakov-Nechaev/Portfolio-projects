package com.yakov.cinema.ui.first_start

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FirstStartPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstOneFragment()
            1 -> FirstTwoFragment()
            2 -> FirstThreeFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}