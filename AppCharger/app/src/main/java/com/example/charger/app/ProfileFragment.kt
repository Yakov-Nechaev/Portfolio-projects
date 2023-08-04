package com.example.charger.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.charger.R
import com.example.charger.app.mvvm.ProfileViewModel
import com.example.charger.data.model.user.UserData
import com.example.charger.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        profileViewModel.takeUserData()

        profileViewModel.userData.observe(viewLifecycleOwner) {
            fillDataUser(it.userData)
        }
    }

    private fun fillDataUser(userData: UserData) {
        binding.textViewUserName.text = userData.userProfile.username
        binding.textViewUserEmail.text = userData.userProfile.emailAddress

        Glide.with(binding.imageUser.context)
            .load(userData.userProfile.profileImageURL)
            .circleCrop()
            .into(binding.imageUser)
    }
}

