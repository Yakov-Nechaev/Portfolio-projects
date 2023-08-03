package com.yakov.cinema.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yakov.cinema.R
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.model.app_model.CollectionMovie
import com.yakov.cinema.data.model.app_model.Team
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.databinding.FragmentDetailsBinding
import com.yakov.cinema.navigation.Navigator
import com.yakov.cinema.recycler.CheckBoxCollectionAdapter
import com.yakov.cinema.recycler.ImagesAdapter
import com.yakov.cinema.recycler.MoviesAdapter
import com.yakov.cinema.recycler.TeamAdapter
import com.yakov.cinema.ui.view_models.DetailsFactory
import com.yakov.cinema.ui.view_models.DetailsViewModel

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel by viewModels<DetailsViewModel> { DetailsFactory(requireContext()) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        val adapterActor = TeamAdapter { onClickCrew(it) }
        binding.recyclerViewActor.adapter = adapterActor
        val adapterPersonnel = TeamAdapter { onClickCrew(it) }
        binding.recyclerViewPersonnel.adapter = adapterPersonnel
        val adapterSimilar = MoviesAdapter { onClickMovie(it) }
        binding.recyclerViewSimilar.adapter = adapterSimilar
        val adapterImages = ImagesAdapter()
        binding.recyclerViewGallery.adapter = adapterImages
        val adapterCheckBoxCollection = CheckBoxCollectionAdapter(
            idItem = id,
            onClickAdd = { onClickCheckBoxAdd(it) },
            onClickRemove = { onClickCheckBoxRemove(it) })
        binding.recyclerViewCollectionBottom.adapter = adapterCheckBoxCollection


        bottomSheetBehavior = BottomSheetBehavior.from<View>(binding.sheetBottom).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight = 0
        }

        binding.textInputLayoutCollectionBottom.setEndIconOnClickListener {
            val text = binding.editTextCollectionBottom.text.toString()
            if (text.isNotEmpty()) viewModel.addNewCollection(text)
            unShowEditAddCollection()
        }

        binding.textViewCreateNewCollectionBottom.setOnClickListener {
            showEditAddCollection()
        }

        binding.imageViewCloseEditCollectionBottom.setOnClickListener {
            unShowEditAddCollection()
        }

        binding.imageViewRemove.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetBehavior.peekHeight = 0
        }

        val id = arguments?.getInt(ARG_ID) ?: 0

        id.let {
            viewModel.apply {
                updateMovie(it)
                updateTeam(it)
                updateSimilar(it)
                updateImages(it)
                updateViewedPorId(it)
                updateFavorite(it)
                updateMarc(it)
                updateAllCollection()
            }
        }

        binding.buttonBottom.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        viewModel.allCollectionList.observe(viewLifecycleOwner) {
            adapterCheckBoxCollection.update(it)
        }

        viewModel.team.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading...")
                is Result.Success -> {
                    adapterActor.update(result.data.actorList)
                    adapterPersonnel.update(result.data.personnelList)

                    binding.textViewCounterActor.text = result.data.actorList.size.toString()
                    binding.textViewCounterPersonnel.text =
                        result.data.personnelList.size.toString()
                }

                is Result.Failure -> showError()
            }
        }

        viewModel.images.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading...")
                is Result.Success -> adapterImages.update(result.data)
                is Result.Failure -> showError()

            }
        }

        viewModel.similarMovie.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading...")
                is Result.Success -> adapterSimilar.update(result.data)
                is Result.Failure -> showError()
            }
        }

        viewModel.stateLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                (requireActivity() as Navigator).showLoadingFragment()
            } else {
                (requireActivity() as Navigator).dismissLoadingFragment()
            }
        }

        viewModel.movie.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d("MyLog", "loading...")
                is Result.Success -> {
                    binding.textViewPrimaryLine.text = result.data.primaryLine
                    binding.textViewSecondaryLine.text = result.data.secondaryLine
                    binding.textViewThirdLine.text = result.data.thirdLine
                    binding.textViewDescriptionShort.text = result.data.shortDescription
                    binding.textViewDescriptionCompletle.text = result.data.completeDescription
                    Glide
                        .with(binding.imageViewPosterDetails.context)
                        .load(result.data.image)
                        .into(binding.imageViewPosterDetails)

                    binding.textViewNameFilmBottom.text = result.data.name
                    binding.textViewGenreFilmBottom.text = result.data.genre
                    Glide
                        .with(binding.imageViewBottomMoviePictures.context)
                        .load(result.data.image)
                        .into(binding.imageViewBottomMoviePictures)
                }

                is Result.Failure -> showError()
            }
        }

        binding.buttonShareInfoMovie.setOnClickListener {
            val movie = (viewModel.movie.value as? Result.Success)?.data
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${movie?.primaryLine} \n ${movie?.secondaryLine} \n ${movie?.thirdLine}"
                )
            }
            val intent = Intent.createChooser(shareIntent, "Share")
            startActivity(intent)
        }

        viewModel.viewed.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.buttonViewedOrNot.setImageResource(R.drawable.icon_viewed)
            } else {
                binding.buttonViewedOrNot.setImageResource(R.drawable.icon_not_viewed)
            }
        }

        binding.buttonViewedOrNot.setOnClickListener {
            if (viewModel.viewed.value == true) viewModel.removeViewed(id)
            else viewModel.addViewed(id)
        }

        viewModel.favorite.observe(viewLifecycleOwner) {
            if (it == true) binding.buttonFavoriteOrNot.setImageResource(R.drawable.icon_like)
            else binding.buttonFavoriteOrNot.setImageResource(R.drawable.icon_not_like)

        }

        binding.buttonFavoriteOrNot.setOnClickListener {
            if (viewModel.favorite.value == true) viewModel.removeFavorite(id)
            else viewModel.addFavorite(id)
        }

        viewModel.marc.observe(viewLifecycleOwner) {
            if (it == true) binding.buttonMarkOrNot.setImageResource(R.drawable.icon_mark)
            else binding.buttonMarkOrNot.setImageResource(R.drawable.icon_not_mark)

        }

        binding.buttonMarkOrNot.setOnClickListener {
            if (viewModel.marc.value == true) viewModel.removeMarc(id)
            else viewModel.addMarc(id)
        }


        binding.textViewCounterActor.setOnClickListener {
            (requireActivity() as Navigator).navigateToShowAllPersonnelFragment(
                type = Constants.SHOW_ALL_ACTORS,
                id = id
            )
        }

        binding.textViewCounterPersonnel.setOnClickListener {
            (requireActivity() as Navigator).navigateToShowAllPersonnelFragment(
                type = Constants.SHOW_ALL_PERSONNEL_TEAM,
                id = id
            )
        }

        binding.textViewCounterGallery.setOnClickListener {
            (requireActivity() as Navigator).navigateToShowAllPhotoFragment(id = id)
        }

        binding.buttonGoBackDetails.setOnClickListener { (requireActivity() as Navigator).goBack() }
    }

    private fun showEditAddCollection() {
        binding.apply {
            cardViewEditCollectionBottom.visibility = View.VISIBLE
            scrollViewDetails.visibility = View.GONE
            sheetBottom.visibility = View.GONE
        }
    }

    private fun unShowEditAddCollection() {
        binding.apply {
            cardViewEditCollectionBottom.visibility = View.GONE
            scrollViewDetails.visibility = View.VISIBLE
            sheetBottom.visibility = View.VISIBLE
        }
    }

    private fun showError() {
        (requireActivity() as Navigator).apply {
            dismissLoadingFragment()
            navigateToErrorFragment(Constants.BACK_HOME)
        }
    }

    private fun onClickCrew(item: Team) {
        (requireActivity() as Navigator).navigateToPersonnelFragment(item.id())
    }

    private fun onClickMovie(briefMovie: BriefMovie) {
        viewModel.saveItemWasInterested(briefMovie.id ?:0)
        (requireActivity() as Navigator).navigateToDetailsFragment(briefMovie.id ?:0)
    }

    private fun onClickCheckBoxAdd(item: CollectionMovie) {
        val idItem = arguments?.getInt(ARG_ID) ?: 0
        viewModel.addItemInCollection(idItem, item.name)
    }

    private fun onClickCheckBoxRemove(item: CollectionMovie) {
        val idItem = arguments?.getInt(ARG_ID) ?: 0
        viewModel.removeItemInCollection(idItem, item.name)
    }

    companion object {
        private const val ARG_ID = "id_movie"
        fun newInstance(id: Int): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_ID, id)
            }
            return fragment
        }
    }
}