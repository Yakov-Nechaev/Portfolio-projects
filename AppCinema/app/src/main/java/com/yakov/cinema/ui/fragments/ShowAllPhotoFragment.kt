package com.yakov.cinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.yakov.cinema.R
import com.yakov.cinema.databinding.FragmentShowAllPhotoBinding
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.navigation.Navigator
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.recycler.ImagesAdapter
import com.yakov.cinema.ui.view_models.AllPhotoViewModel
import com.yakov.cinema.ui.view_models.AllPhotoFactory

class ShowAllPhotoFragment : Fragment(R.layout.fragment_show_all_photo) {

    private lateinit var binding: FragmentShowAllPhotoBinding
    private val viewModel by viewModels<AllPhotoViewModel> { AllPhotoFactory(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShowAllPhotoBinding.bind(view)
        val id = arguments?.getInt(ID_KEY) ?: 0
        viewModel.updateImages(id)

        val adapterPhoto = ImagesAdapter()
        binding.recyclerViewShowAllPhoto.adapter = adapterPhoto

        viewModel.images.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> (requireActivity() as Navigator).showLoadingFragment()
                is Result.Success -> {
                    (requireActivity() as Navigator).dismissLoadingFragment()
                    adapterPhoto.update(result.data)
                }

                is Result.Failure -> showError()
            }
        }

        binding.buttonBackAllPhoto.setOnClickListener {
            (requireActivity() as Navigator).goBack()
        }
    }

    private fun showError() {
        (requireActivity() as Navigator).apply {
            dismissLoadingFragment()
            navigateToErrorFragment(Constants.BACK_HOME)
        }
    }

    companion object {
        private const val ID_KEY = "id"

        fun newInstance(id: Int): ShowAllPhotoFragment {
            val fragment = ShowAllPhotoFragment()
            fragment.arguments = Bundle().apply {
                putInt(ID_KEY, id)
            }
            return fragment
        }
    }
}