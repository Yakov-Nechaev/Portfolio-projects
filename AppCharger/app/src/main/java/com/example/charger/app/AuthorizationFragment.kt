package com.example.charger.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.charger.R
import com.example.charger.app.mvvm.AuthorizationViewModel
import com.example.charger.app.navigator.Navigator
import com.example.charger.data.model.user.AuthRequest
import com.example.charger.databinding.FragmentAuthorizationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    private lateinit var binding: FragmentAuthorizationBinding
    private val authorizationViewModel: AuthorizationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthorizationBinding.bind(view)

        binding.login.setOnClickListener {
            val email = binding.editTextUsername.text.toString()
            val password = binding.editTextKey.text.toString()
            val authRequest = AuthRequest(email, password)

            authorizationViewModel.takeToken(authRequest)
            authorizationViewModel.saveRequest(authRequest)
        }

        binding.create.setOnClickListener {
            val websiteUrl = "https://openchargemap.org/site/loginprovider/beginlogin"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }

        binding.textViewForgot.setOnClickListener {
            val websiteUrl = "https://openchargemap.org/site"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }

        authorizationViewModel.token.observe(viewLifecycleOwner) { token ->
            if (token != null && binding.editTextKey.text.length > 4) {
                (requireActivity() as Navigator).navigateToMapFragment()
            } else {
                Toast.makeText(requireContext(), R.string.failed_message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}