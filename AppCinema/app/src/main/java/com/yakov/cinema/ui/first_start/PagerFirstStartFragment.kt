package com.yakov.cinema.ui.first_start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.viewpager2.widget.ViewPager2
import com.yakov.cinema.R
import com.yakov.cinema.databinding.FragmentPagerFirstStartBinding
import com.yakov.cinema.navigation.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PagerFirstStartFragment : Fragment(R.layout.fragment_pager_first_start) {

    private lateinit var binding: FragmentPagerFirstStartBinding
    lateinit var adapter: FirstStartPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPagerFirstStartBinding.bind(view)
        adapter = FirstStartPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPagerFirstStart.adapter = adapter

        val job = viewLifecycleOwner.lifecycle.coroutineScope.launch {
            var position = 0
            repeat(3) {
                binding.viewPagerFirstStart.setCurrentItem(position, true)
                delay(2000)
                position++
            }
            (requireActivity() as Navigator).apply {
                navigateToPrimaryFragment()
                cleanSecondaryContainer()
            }
        }

        binding.viewPagerFirstStart.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    job.cancel()
                }
            }
        })
    }

}

