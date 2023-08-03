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
import com.yakov.cinema.databinding.FragmentPrimaryBinding
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.navigation.Navigator
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.recycler.MoviesAdapter
import com.yakov.cinema.ui.view_models.PrimaryViewModel
import com.yakov.cinema.ui.view_models.PrimaryFactory
import kotlinx.coroutines.launch

class PrimaryFragment : Fragment(R.layout.fragment_primary) {

    private lateinit var binding: FragmentPrimaryBinding
    private val viewModel by viewModels<PrimaryViewModel> { PrimaryFactory(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrimaryBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateTopShort()
            viewModel.updatePremieres()
            viewModel.updatePopularShort()
            viewModel.updateActionUSA()
            viewModel.updateDramFrance()
            viewModel.updateSoap()
        }

        val adapterTop = MoviesAdapter { chooseMovie(it) }
        binding.recyclerViewTop.adapter = adapterTop
        val adapterPremieres = MoviesAdapter { chooseMovie(it) }
        binding.recyclerViewPremieres.adapter = adapterPremieres
        val adapterPopular = MoviesAdapter { chooseMovie(it) }
        binding.recyclerViewPopular.adapter = adapterPopular
        val adapterActionUSA = MoviesAdapter { chooseMovie(it) }
        binding.recyclerViewActionsUSA.adapter = adapterActionUSA
        val adapterDramFrance = MoviesAdapter { chooseMovie(it) }
        binding.recyclerViewDramFrance.adapter = adapterDramFrance
        val adapterSoap = MoviesAdapter { chooseMovie(it) }
        binding.recyclerViewSoap.adapter = adapterSoap

        viewModel.stateLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                (requireActivity() as Navigator).showLoadingFragment()
            } else {
                (requireActivity() as Navigator).dismissLoadingFragment()
            }
        }

        viewModel.listDramFrance.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading")
                is Result.Success -> adapterDramFrance.update(result.data)
                is Result.Failure -> {
                    showError()
                    Toast.makeText(requireContext(), "drama", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.listSoap.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading")
                is Result.Success -> adapterSoap.update(result.data)
                is Result.Failure -> {
                    showError()
                    Toast.makeText(requireContext(), "soap", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.listActionUSA.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading")
                is Result.Success -> adapterActionUSA.update(result.data)
                is Result.Failure -> showError()
            }
        }

        viewModel.listPopular.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading")
                is Result.Success -> adapterPopular.update(result.data)
                is Result.Failure -> {
                    showError()
                    Toast.makeText(requireContext(), "popular", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.listTopResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading")
                is Result.Success -> adapterTop.update(result.data)
                is Result.Failure -> {
                    showError()
                    Toast.makeText(requireContext(), "250", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.listPremieres.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading")
                is Result.Success -> adapterPremieres.update(result.data)
                is Result.Failure -> {
                    showError()
                    Toast.makeText(requireContext(), "premieres", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.textAllPopular.setOnClickListener {
            (requireActivity() as Navigator).navigateToShowAllFragment(Constants.SHOW_ALL_POPULAR)
        }

        binding.textAllPremier.setOnClickListener {
            (requireActivity() as Navigator).navigateToShowAllFragment(Constants.SHOW_ALL_PREMIERES)
        }

        binding.textAllActionUsa.setOnClickListener {
            (requireActivity() as Navigator).navigateToShowAllFragment(Constants.SHOW_ALL_ACTION_USA)
        }

        binding.textAllTop.setOnClickListener {
            (requireActivity() as Navigator).navigateToShowAllFragment(Constants.SHOW_ALL_TOP_250)
        }

        binding.textAllDramFrance.setOnClickListener {
            (requireActivity() as Navigator).navigateToShowAllFragment(Constants.SHOW_ALL_DRAMA_FRANCE)
        }

        binding.textAllSoap.setOnClickListener {
            (requireActivity() as Navigator).navigateToShowAllFragment(Constants.SHOW_ALL_SOAP)
        }
    }

    private fun showError() {
        (requireActivity() as Navigator).apply {
            dismissLoadingFragment()
            navigateToErrorFragment(Constants.BACK_HOME)
        }
    }

    private fun chooseMovie(briefMovie: BriefMovie) {
        viewModel.saveItemWasInterested(briefMovie.id ?:0)
        (requireActivity() as Navigator).navigateToDetailsFragment(briefMovie.id ?:0)
    }
}