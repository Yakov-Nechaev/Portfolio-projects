package com.yakov.cinema.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.yakov.cinema.R
import com.yakov.cinema.databinding.FragmentLoadingBinding

class LoadingFragment : DialogFragment(R.layout.fragment_loading) {

    private lateinit var binding: FragmentLoadingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoadingBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 1)
        val height = (resources.displayMetrics.heightPixels * 1)
        dialog?.window?.setLayout(width, height)
    }
}