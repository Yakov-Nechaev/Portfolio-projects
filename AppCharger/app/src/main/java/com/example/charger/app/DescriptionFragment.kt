package com.example.charger.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.charger.R
import com.example.charger.app.model.Coordinates
import com.example.charger.app.mvvm.DescriptionViewModel
import com.example.charger.app.navigator.Navigator
import com.example.charger.data.model.ChargingLocation
import com.example.charger.databinding.FragmentDescriptionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class DescriptionFragment : Fragment(R.layout.fragment_description) {

    private lateinit var binding: FragmentDescriptionBinding
    private val descriptionViewModel: DescriptionViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDescriptionBinding.bind(view)
        arguments?.getString(CHARGING_ID)?.let { descriptionViewModel.findCharging(it) }

        descriptionViewModel.selectedCharging.observe(viewLifecycleOwner) {
            fillData(it)
        }

        binding.buttonRouteComplete.setOnClickListener {
            val coordinates = takeCoordinates()
            (requireActivity() as Navigator).goToRouteMapActivity(coordinates)
        }

        binding.buttonCallComplete.setOnClickListener {
            val phone = takePhone()
            (requireActivity() as Navigator).goToPhoneCallActivity(phone)
        }

        binding.buttonShareComplete.setOnClickListener {
            val coordinates = takeCoordinates()
            val title = takeTitle()
            (requireActivity() as Navigator).goToShareActivity(coordinates, title)
        }

        binding.imageViewBack.setOnClickListener {
            (requireActivity() as Navigator).goBack()
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

    private fun fillData(chargingLocation: ChargingLocation) {
        binding.textViewTitleComplete.text =
            getString(R.string.azs_title, chargingLocation.operatorInfo.title)
        binding.textViewIdComplete.text =
            getString(R.string.azs_id, chargingLocation.id.toString())
        binding.textViewLocationComplete.text = getString(
            R.string.location_info,
            chargingLocation.addressInfo.addressLine1,
            chargingLocation.addressInfo.stateOrProvince
        )
        binding.textViewPowerComplete.text =
            chargingLocation.connections.firstOrNull()?.powerKW.toString()
        binding.textViewCostComplete.text = chargingLocation.usageCost
        binding.textViewNumberComplete.text = chargingLocation.connections.size.toString()
        binding.textViewLat.text = chargingLocation.addressInfo.latitude.toString()
        binding.textViewLon.text = chargingLocation.addressInfo.longitude.toString()
        binding.textViewLastDate.text = formatDate(chargingLocation.dateLastVerified)
        binding.textViewLink.text = chargingLocation.operatorInfo.websiteURL
        binding.textViewPhone.text = chargingLocation.operatorInfo.phonePrimaryContact
    }

    private fun formatDate(date: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateInput = inputFormat.parse(date)
        return dateInput?.let { outputFormat.format(it) }
    }

    companion object {
        private const val CHARGING_ID = "ID"

        fun newInstance(id: String): DescriptionFragment {
            val fragment = DescriptionFragment()
            val bundle = Bundle().apply { putString(CHARGING_ID, id) }
            fragment.arguments = bundle
            return fragment
        }
    }
}