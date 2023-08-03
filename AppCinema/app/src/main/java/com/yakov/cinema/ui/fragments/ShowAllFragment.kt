package com.yakov.cinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yakov.cinema.R
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.databinding.FragmentShowAllBinding
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.navigation.Navigator
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.recycler.MoviesAdapter
import com.yakov.cinema.ui.view_models.AllMovieViewModel
import com.yakov.cinema.ui.view_models.AllMovieFactory

class ShowAllFragment : Fragment(R.layout.fragment_show_all) {

    private lateinit var binding: FragmentShowAllBinding
    val viewModel by viewModels<AllMovieViewModel> { AllMovieFactory(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShowAllBinding.bind(view)
        val type = arguments?.getString(TYPE_KEY).toString()
        val adapterAll = MoviesAdapter { onClick(it) }
        binding.recyclerViewShowAll.adapter = adapterAll

        viewModel.fillData(type)

        binding.textViewTitleAll.text = when (type) {
            Constants.SHOW_ALL_SOAP -> getString(R.string.soap)
            Constants.SHOW_ALL_TOP_250 -> getString(R.string.top_250)
            Constants.SHOW_ALL_POPULAR -> getString(R.string.popular)
            Constants.SHOW_ALL_PREMIERES -> getString(R.string.premieres)
            Constants.SHOW_ALL_ACTION_USA -> getString(R.string.action_usa)
            Constants.SHOW_ALL_DRAMA_FRANCE -> getString(R.string.dramas_france)
            else -> ""
        }

        binding.recyclerViewShowAll.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1 && viewModel.stateProgressLoading.value == false) {
                    viewModel.fillData(type)
                }
            }
        })

        viewModel.stateProgressLoading.observe(viewLifecycleOwner) {
            if (it == true) binding.progressBarLoading.visibility = View.VISIBLE
            else binding.progressBarLoading.visibility = View.GONE
        }

        viewModel.allMovieList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> (requireActivity() as Navigator).showLoadingFragment()
                is Result.Success -> {
                    (requireActivity() as Navigator).dismissLoadingFragment()
                    adapterAll.update(result.data)
                }

                else -> showError()
            }
        }

        binding.buttonBackAll.setOnClickListener {
            (requireActivity() as Navigator).goBack()
        }
    }

    private fun onClick(briefMovie: BriefMovie) {
        (requireActivity() as Navigator).navigateToDetailsFragment(briefMovie.id)
    }

    private fun showError() {
        (requireActivity() as Navigator).apply {
            dismissLoadingFragment()
            navigateToErrorFragment(Constants.BACK_HOME)
        }
    }

    companion object {
        private const val TYPE_KEY = "type"

        fun newInstance(name: String): ShowAllFragment {
            val fragment = ShowAllFragment()
            fragment.arguments = Bundle().apply {
                putString(TYPE_KEY, name)
            }
            return fragment
        }
    }
}