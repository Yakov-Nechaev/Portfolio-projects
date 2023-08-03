package com.yakov.cinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.yakov.cinema.R
import com.yakov.cinema.data.model.app_model.Team
import com.yakov.cinema.databinding.FragmentShowAllPersonnelBinding
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.navigation.Navigator
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.recycler.TeamAdapter
import com.yakov.cinema.ui.view_models.AllPersonnelViewModel
import com.yakov.cinema.ui.view_models.AllPersonnelFactory

class ShowAllPersonnelFragment : Fragment(R.layout.fragment_show_all_personnel) {

    private lateinit var binding: FragmentShowAllPersonnelBinding
    private val viewModel by viewModels<AllPersonnelViewModel> { AllPersonnelFactory(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShowAllPersonnelBinding.bind(view)
        val type = arguments?.getString(TYPE_KEY).toString()
        val id = arguments?.getInt(ID_KEY) ?: 0

        viewModel.updateTeam(id)

        val adapterTeam = TeamAdapter { onClickCrew(it) }
        binding.recyclerViewShowAllPersonnel.adapter = adapterTeam

        binding.textViewTitleAllPersonnel.text =
            if (type == Constants.SHOW_ALL_ACTORS) getString(R.string.actors)
            else getString(R.string.crew)

        binding.buttonBackAllPersonnel.setOnClickListener {
            (requireActivity() as Navigator).goBack()
        }

        viewModel.team.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> (requireActivity() as Navigator).showLoadingFragment()
                is Result.Success -> {
                    (requireActivity() as Navigator).dismissLoadingFragment()
                    if (type == Constants.SHOW_ALL_ACTORS) {
                        adapterTeam.update(result.data.actorList)
                    } else {
                        adapterTeam.update(result.data.personnelList)
                    }
                }

                is Result.Failure -> showError()
            }
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

    companion object {
        private const val TYPE_KEY = "type"
        private const val ID_KEY = "id"

        fun newInstance(name: String, id: Int): ShowAllPersonnelFragment {
            val dialogFragment = ShowAllPersonnelFragment()
            dialogFragment.arguments = Bundle().apply {
                putString(TYPE_KEY, name)
                putInt(ID_KEY, id)
            }
            return dialogFragment
        }
    }
}