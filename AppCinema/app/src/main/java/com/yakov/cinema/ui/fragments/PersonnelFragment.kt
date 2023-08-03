package com.yakov.cinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.yakov.cinema.R
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.model.network_model.FilmPerson
import com.yakov.cinema.databinding.FragmentPersonnelBinding
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.navigation.Navigator
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.recycler.MoviesAdapter
import com.yakov.cinema.recycler.ShortDataMovieAdapter
import com.yakov.cinema.ui.view_models.PersonnelViewModel
import com.yakov.cinema.ui.view_models.PersonnelFactory

class PersonnelFragment : Fragment(R.layout.fragment_personnel) {

    private lateinit var binding: FragmentPersonnelBinding
    private val viewModel by viewModels<PersonnelViewModel> { PersonnelFactory(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPersonnelBinding.bind(view)
        val id = arguments?.getInt(ARG_ID)

        val adapterBestMovie = MoviesAdapter { onClickMovie(it) }
        binding.recyclerViewBestFilmPersonnel.adapter = adapterBestMovie

        val adapterShortMovie = ShortDataMovieAdapter { onClickShortMovie(it) }
        binding.recyclerViewShortMovie.adapter = adapterShortMovie

        id?.let { viewModel.updatePersonnel(it) }

        viewModel.personnel.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> (requireActivity() as Navigator).showLoadingFragment()
                is Result.Success -> {
                    (requireActivity() as Navigator).dismissLoadingFragment()
                    Glide
                        .with(binding.imageViewPersonnelInfo.context)
                        .load(result.data.photo)
                        .into(binding.imageViewPersonnelInfo)
                    binding.textViewPersonnelInfo.text = result.data.name
                    binding.textViewRolePersonnel.text = result.data.profession
                    binding.textViewCounterPersonnelAllMovie.text =
                        result.data.allIdMovieId.size.toString()
                    adapterBestMovie.update(result.data.bestMovie)
                    adapterBestMovie.hardUpdate()
                    adapterShortMovie.update(result.data.listMovieFull)
                }

                is Result.Failure -> showError()
            }
        }

        binding.textViewShowAllPersonnelMovie.setOnClickListener {
            if (binding.recyclerViewShortMovie.visibility == View.GONE) {
                binding.recyclerViewShortMovie.visibility = View.VISIBLE
                binding.recyclerViewBestFilmPersonnel.visibility = View.GONE
                binding.textViewBestFilmPersonnel.visibility = View.GONE
                binding.textViewShowAllPersonnelMovie.text =
                    getString(R.string.not_show_personnel_all_movie)
            } else {
                binding.recyclerViewShortMovie.visibility = View.GONE
                binding.recyclerViewBestFilmPersonnel.visibility = View.VISIBLE
                binding.textViewBestFilmPersonnel.visibility = View.VISIBLE
                binding.textViewShowAllPersonnelMovie.text = getString(R.string.to_the_list)
            }
        }

        binding.buttonBackPersonnel.setOnClickListener {
            (requireActivity() as Navigator).goBack()
        }
    }

    private fun onClickMovie(briefMovie: BriefMovie) {
        (requireActivity() as Navigator).navigateToDetailsFragment(briefMovie.id)
    }

    private fun onClickShortMovie(filmPerson: FilmPerson) {
        (requireActivity() as Navigator).navigateToDetailsFragment(filmPerson.filmId ?: 0)
    }

    private fun showError() {
        (requireActivity() as Navigator).apply {
            dismissLoadingFragment()
            navigateToErrorFragment(Constants.BACK_HOME)
        }
    }

    companion object {
        private const val ARG_ID = "id_personnel"
        fun newInstance(id: Int): PersonnelFragment {
            val fragment = PersonnelFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_ID, id)
            }
            return fragment
        }
    }
}
