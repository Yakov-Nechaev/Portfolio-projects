package com.yakov.cinema.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yakov.cinema.R
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.model.app_model.CollectionMovie
import com.yakov.cinema.databinding.FragmentProfileBinding
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.navigation.Navigator
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.recycler.CollectionAdapter
import com.yakov.cinema.recycler.MoviesAdapter
import com.yakov.cinema.ui.view_models.ProfileViewModel
import com.yakov.cinema.ui.view_models.ProfileFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel> { ProfileFactory(requireContext()) }
    private lateinit var adapterDetails: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateWasInterested()
            viewModel.updateViewed()
            viewModel.fillListCollection()
            viewModel.updateWelcome()
        }

        val adapterWasInterested = MoviesAdapter { onClickWasInterested(it) }
        binding.recyclerViewWasInterested.adapter = adapterWasInterested
        val adapterViewed = MoviesAdapter { onClickViewed(it) }
        binding.recyclerViewViewed.adapter = adapterViewed
        val adapterCollection = CollectionAdapter(onClickChoose = { chooseCollection(it) },
            onClickDelete = { deleteCollection(it) })
        binding.recyclerViewCollection.adapter = adapterCollection
        adapterDetails = MoviesAdapter { onClickViewed(it) }

        binding.textViewCreateNewCollection.setOnClickListener {
            binding.scrollViewProfile.visibility = View.GONE
            binding.cardViewEditCollection.visibility = View.VISIBLE
        }

        binding.textInputLayoutCollection.setEndIconOnClickListener {
            val text = binding.editTextCollection.text.toString()
            if (text.isNotEmpty()) viewModel.addNewCollection(text)
            binding.scrollViewProfile.visibility = View.VISIBLE
            binding.cardViewEditCollection.visibility = View.GONE
        }

        binding.imageViewCloseEditCollection.setOnClickListener {
            binding.scrollViewProfile.visibility = View.VISIBLE
            binding.cardViewEditCollection.visibility = View.GONE
        }

        viewModel.listCollection.observe(viewLifecycleOwner) {
            adapterCollection.update(it)
        }

        viewModel.stateLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) (requireActivity() as Navigator).showLoadingFragment()
            else (requireActivity() as Navigator).dismissLoadingFragment()
        }

        viewModel.listWasInterested.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading...")
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        adapterWasInterested.update(result.data)
                        binding.textViewAllWasInterested.text = ""
                    } else {
                        adapterWasInterested.updateWithLastClean(result.data)
                        binding.textViewAllWasInterested.text = result.data.size.toString()
                    }
                }

                is Result.Failure -> showError()
            }
        }

        viewModel.listViewed.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading...")
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        adapterViewed.update(result.data)
                        binding.textViewViewedCounter.text = ""
                    } else {
                        adapterViewed.updateWithLastClean(result.data)
                        binding.textViewViewedCounter.text = result.data.size.toString()
                    }
                }

                is Result.Failure -> showError()
            }
        }

        binding.imageViewBackToCollection.setOnClickListener {
            binding.layoutWithRecycler.visibility = View.GONE
            binding.scrollViewProfile.visibility = View.VISIBLE
        }

        viewModel.welcome.observe(viewLifecycleOwner) {
            binding.checkBoxShowFirstStart.isChecked = it
        }

        binding.checkBoxShowFirstStart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.saveWithWelcome()
            else viewModel.saveWithoutWelcome()
        }

        viewModel.listMovieFromCollection.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading...")
                is Result.Success -> adapterDetails.update(result.data)
                is Result.Failure -> showError()
            }
        }

        binding.textViewAllWasInterested.setOnClickListener {
            binding.layoutWithRecycler.visibility = View.VISIBLE
            binding.scrollViewProfile.visibility = View.GONE
            binding.textViewNameDetails.text = binding.textViewWasInterestedInfo.text
            binding.recyclerDetailsMoviesCollection.adapter = adapterWasInterested
        }

        binding.textViewViewedCounter.setOnClickListener {
            binding.layoutWithRecycler.visibility = View.VISIBLE
            binding.scrollViewProfile.visibility = View.GONE
            binding.textViewNameDetails.text = binding.textViewViewedInfo.text
            binding.recyclerDetailsMoviesCollection.adapter = adapterViewed
        }
    }

    private fun chooseCollection(item: CollectionMovie) {
        binding.layoutWithRecycler.visibility = View.VISIBLE
        binding.scrollViewProfile.visibility = View.GONE
        binding.textViewNameDetails.text = item.name
        viewModel.updateListMovieFromCollection(item.name)
        binding.recyclerDetailsMoviesCollection.adapter = adapterDetails
    }

    private fun showError() {
        (requireActivity() as Navigator).apply {
            dismissLoadingFragment()
            navigateToErrorFragment(Constants.BACK_PROFILE)
        }
    }

    private fun onClickWasInterested(item: BriefMovie) {
        if (item.id == Constants.LAST_ITEM_ID) viewModel.clearDataWasInterested()
        else (requireActivity() as Navigator).navigateToDetailsFragment(item.id)
    }

    private fun onClickViewed(item: BriefMovie) {
        if (item.id == Constants.LAST_ITEM_ID) viewModel.clearDataViewed()
        else (requireActivity() as Navigator).navigateToDetailsFragment(item.id)
    }

    private fun deleteCollection(item: CollectionMovie) {
        viewModel.deleteCollection(item.name)
    }

}