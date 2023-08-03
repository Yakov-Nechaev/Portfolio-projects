package com.yakov.cinema.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yakov.cinema.R
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.databinding.FragmentSearchBinding
import com.yakov.cinema.navigation.Navigator
import com.yakov.cinema.recycler.MoviesAdapter
import com.yakov.cinema.ui.view_models.SearchFactory
import com.yakov.cinema.ui.view_models.SearchViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var binding: FragmentSearchBinding
    val viewModel by viewModels<SearchViewModel> { SearchFactory(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        viewModel.apply {
            setKeyword(binding.textEditSearch.text.toString())
            getSearch()
        }

        arguments?.getString(PARAMS)?.let {
            binding.layoutSearch.visibility = View.GONE
            binding.layoutParams.visibility = View.VISIBLE
        }

        val adapter = MoviesAdapter { onClick(it) }
        binding.recyclerSearch.adapter = adapter

        binding.spinnerCountry.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.Countries_key)
        )

        binding.spinnerGenre.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.Genre_key)
        )

        binding.typeLeftButton.setOnClickListener {
            val typeList = resources.getStringArray(R.array.type_movie)
            val type = typeList[0]
            viewModel.setType(type)
        }
        binding.typeCenterButton.setOnClickListener {
            val typeList = resources.getStringArray(R.array.type_movie)
            val type = typeList[1]
            viewModel.setType(type)
        }
        binding.typeRightButton.setOnClickListener {
            val typeList = resources.getStringArray(R.array.type_movie)
            val type = typeList[2]
            viewModel.setType(type)
        }

        binding.sortLeftButton.setOnClickListener {
            val sortList = resources.getStringArray(R.array.order)
            val order = sortList[0]
            viewModel.setOrder(order)
        }
        binding.sortCenterButton.setOnClickListener {
            val sortList = resources.getStringArray(R.array.order)
            val order = sortList[1]
            viewModel.setOrder(order)
        }
        binding.sortRightButton.setOnClickListener {
            val sortList = resources.getStringArray(R.array.order)
            val order = sortList[2]
            viewModel.setOrder(order)
        }

        binding.spinnerCountry.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCountry =
                        resources.getIntArray(R.array.Countries_int_value)[position]
                    viewModel.setCountry(selectedCountry)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.spinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGenre = resources.getIntArray(R.array.Genre_int_value)[position]
                viewModel.setGenre(selectedGenre)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.buttonDefaultParams.setOnClickListener {
            (requireActivity() as Navigator).navigateToSearchParamsFragment()

        }

        binding.yearsSeekBar.addOnChangeListener { slider, _, _ ->
            val minValue = slider.values.first().toInt()
            val maxValue = slider.values.last().toInt()

            if (minValue == 1950 && maxValue == 2023) binding.textViewYearCurrent.text =
                getString(R.string.any)
            else binding.textViewYearCurrent.text = buildString {
                append("С ")
                append(minValue)
                append(" - до ")
                append(maxValue)
                append(" гг")
            }

            viewModel.setYearFrom(minValue)
            viewModel.setYearTo(maxValue)
        }

        binding.ratingSeekBar.addOnChangeListener { slider, _, _ ->
            val minValue = slider.values.first().toInt()
            val maxValue = slider.values.last().toInt()

            if (minValue == 0 && maxValue == 10) binding.textViewRatingCurrent.text =
                getString(R.string.any)
            else binding.textViewRatingCurrent.text = buildString {
                append(minValue)
                append(" - ")
                append(maxValue)
            }

            viewModel.setRatingFrom(minValue)
            viewModel.setRatingTo(maxValue)
        }


        viewModel.listMovie.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    adapter.update(mutableListOf())
                    binding.progressBarSearch.visibility = View.VISIBLE
                    binding.textViewNothingFound.visibility = View.GONE
                }

                is Result.Success -> {
                    binding.progressBarSearch.visibility = View.GONE
                    if (result.data.isEmpty()) {
                        binding.textViewNothingFound.visibility = View.VISIBLE
                    } else {
                        adapter.update(result.data)
                    }
                }

                is Result.Failure -> {
                    binding.progressBarSearch.visibility = View.GONE
                    binding.textViewNothingFound.visibility = View.VISIBLE
                }
            }

        }

        binding.textInputLayoutSearch.setStartIconOnClickListener {
            binding.layoutSearch.visibility = View.GONE
            binding.layoutParams.visibility = View.VISIBLE
        }

        binding.buttonSearchVisible.setOnClickListener {
            binding.layoutSearch.visibility = View.VISIBLE
            binding.layoutParams.visibility = View.GONE
        }

        binding.textInputLayoutSearch.setEndIconOnClickListener {
            viewModel.getSearch()
        }

        binding.textEditSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.setKeyword(text.toString())

        }
    }

    private fun onClick(briefMovie: BriefMovie) {
        viewModel.saveItemWasInterested(briefMovie.id)
        (requireActivity() as Navigator).navigateToDetailsFragment(briefMovie.id)
    }

    companion object {
        private const val PARAMS = "params"

        fun newInstance(params: String): SearchFragment {
            val fragment = SearchFragment()
            fragment.arguments = Bundle().apply {
                putString(PARAMS, params)
            }
            return fragment
        }
    }
}