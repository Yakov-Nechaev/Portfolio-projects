package com.example.charger.app

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.charger.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.charger.app.model.Bounds
import com.example.charger.app.mvvm.MapViewModel
import com.example.charger.app.navigator.Navigator
import com.example.charger.databinding.FragmentMapsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : Fragment(R.layout.fragment_maps) {

    private lateinit var binding: FragmentMapsBinding
    private val viewModel: MapViewModel by viewModel()
    private var currentLocationMarker: Marker? = null

    private val callback = OnMapReadyCallback { googleMap ->
        setupMap(googleMap)
        showCurrentLocation(googleMap)
        fillMarkers(googleMap)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapsBinding.bind(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setupMap(googleMap: GoogleMap) {
        if (getCurrentMode(requireContext()) == 2) {
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.aubergine_map_style
                )
            )
        } else {
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.standart_map_style
                )
            )
        }

        googleMap.setOnMarkerClickListener { marker ->
            marker.title?.let { viewModel.selectById(it) }
            viewModel.saveNewMarkerPosition(marker.position.latitude, marker.position.longitude)
            val chargingId = viewModel.selectedChargingLocation.value?.id.toString()
            (requireActivity() as Navigator).navigateToDescriptionFragment(chargingId)
            true
        }

        viewModel.markerPosition.observe(viewLifecycleOwner) { coordinates ->
            val markerLocation = LatLng(coordinates.latitude ?:0.0, coordinates.longitude ?:0.0)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerLocation, 15f)
            googleMap.animateCamera(cameraUpdate)
        }

        googleMap.setOnCameraIdleListener {
            val visibleRegion = googleMap.projection.visibleRegion
            val bounds = Bounds(
                visibleRegion.latLngBounds.southwest.latitude,
                visibleRegion.latLngBounds.southwest.longitude,
                visibleRegion.latLngBounds.northeast.latitude,
                visibleRegion.latLngBounds.northeast.longitude
            )
            viewModel.fetchChargingLocations(bounds)
        }
    }

    private fun showCurrentLocation(googleMap: GoogleMap) {
        if (hasLocationPermission()) {
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    currentLocationMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(currentLocation)
                            .title("My location")
                    )
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 15f)
                    googleMap.animateCamera(cameraUpdate)
                }
            }
        }
    }

    private fun fillMarkers(googleMap: GoogleMap) {
        viewModel.chargingLocations.observe(viewLifecycleOwner) { chargingLocations ->
            googleMap.clear()

            currentLocationMarker?.let {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(it.position)
                        .title(it.title)
                )
            }

            for (station in chargingLocations) {
                val location = LatLng(station.addressInfo.latitude, station.addressInfo.longitude)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(station.id.toString())
                        .icon(getIconMarker())
                )
            }
        }
    }

    private fun getIconMarker(): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.electric_charging_station)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, false)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocationPermission = ACCESS_FINE_LOCATION
        return ContextCompat.checkSelfPermission(
            requireContext(),
            fineLocationPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCurrentMode(context: Context): Int {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiModeManager.nightMode
    }
}