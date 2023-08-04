package com.example.charger.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.charger.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.example.charger.app.model.Coordinates
import com.example.charger.app.mvvm.DescriptionViewModel
import com.example.charger.app.navigator.Navigator
import com.example.charger.data.model.ChargingLocation
import com.example.charger.databinding.FragmentBottomSheetBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetFragment : Fragment(R.layout.fragment_bottom_sheet) {

    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val descriptionViewModel: DescriptionViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetBinding.bind(view)
        arguments?.getString(CHARGING_ID)?.let { descriptionViewModel.findCharging(it) }
        setupBottomSheetBehavior()

        descriptionViewModel.selectedCharging.observe(viewLifecycleOwner) { chargingLocation ->
            updateBottomSheetContent(chargingLocation)
        }

        binding.buttonRoute.setOnClickListener {
            val coordinates = takeCoordinates()
            (requireActivity() as Navigator).goToRouteMapActivity(coordinates)
        }

        binding.buttonCall.setOnClickListener {
            val phone = takePhone()
            (requireActivity() as Navigator).goToPhoneCallActivity(phone)
        }

        binding.buttonShare.setOnClickListener {
            val coordinates = takeCoordinates()
            val title = takeTitle()
            (requireActivity() as Navigator).goToShareActivity(coordinates, title)
        }

        binding.textViewTitle.setOnClickListener {
            val chargingId = descriptionViewModel.selectedCharging.value?.id.toString()
            (requireActivity() as Navigator).navigateToCompleteDescriptionFragment(chargingId)
        }
    }

    private fun takeCoordinates(): Coordinates {
        return Coordinates(
            latitude = descriptionViewModel.selectedCharging.value?.addressInfo?.latitude,
            longitude = descriptionViewModel.selectedCharging.value?.addressInfo?.longitude
        )
    }

    private fun takePhone(): String {
        return descriptionViewModel.selectedCharging.value?.operatorInfo?.phonePrimaryContact.toString()
    }

    private fun takeTitle(): String {
        return descriptionViewModel.selectedCharging.value?.operatorInfo?.title.toString()
    }

    private fun setupBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from<View>(binding.sheetBottom).apply {
            peekHeight = resources.getDimensionPixelSize(R.dimen.peek_height)
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun updateBottomSheetContent(chargingLocation: ChargingLocation) {
        binding.textViewTitle.text =
            getString(R.string.azs_title, chargingLocation.operatorInfo.title)
        binding.textViewId.text =
            getString(R.string.azs_id, chargingLocation.id.toString())
        binding.textViewLocation.text = getString(
            R.string.location_info,
            chargingLocation.addressInfo.addressLine1,
            chargingLocation.addressInfo.stateOrProvince
        )
        binding.textViewPower.text = chargingLocation.connections.firstOrNull()?.powerKW.toString()
        binding.textViewCost.text = chargingLocation.usageCost
        binding.textViewNumber.text = chargingLocation.connections.size.toString()
    }

    companion object {
        private const val CHARGING_ID = "ID"

        fun newInstance(id: String): BottomSheetFragment {
            val fragment = BottomSheetFragment()
            val bundle = Bundle().apply { putString(CHARGING_ID, id) }
            fragment.arguments = bundle
            return fragment
        }
    }
}