package com.example.charger.app

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.charger.R
import com.example.charger.app.model.Coordinates
import com.example.charger.app.navigator.Navigator
import com.example.charger.databinding.ActivityMainBinding

private const val LOCATION_PERMISSION_REQUEST_CODE = 100

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            navigateToAuthorizationFragment()
        }
        else {
            navigateToMapFragment()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.map_menu -> navigateToMapFragment()
                R.id.user_menu -> navigateToAccountFragment()
            }
            true
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocationPermission = ACCESS_FINE_LOCATION
        return ContextCompat.checkSelfPermission(
            this,
            fineLocationPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        val fineLocationPermission = ACCESS_FINE_LOCATION
        ActivityCompat.requestPermissions(
            this,
            arrayOf(fineLocationPermission),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            val fineLocationPermission = ACCESS_FINE_LOCATION
            val fineLocationPermissionGranted =
                permissions.contains(fineLocationPermission) && grantResults[permissions.indexOf(
                    fineLocationPermission
                )] == PackageManager.PERMISSION_GRANTED

            if (fineLocationPermissionGranted) {
                val message = getString(R.string.permission_granted)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.fragment_container_full)
                if (currentFragment != null) {
                    supportFragmentManager.beginTransaction()
                        .remove(currentFragment)
                        .commit()
                }

                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, MapsFragment())
                    .setReorderingAllowed(true)
                    .commit()

            } else {
                val message = getString(R.string.permission_not_granted)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun navigateToMapFragment() {
        if (!hasLocationPermission()) requestLocationPermission()
        else {

            val currentFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container_full)
            if (currentFragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(currentFragment)
                    .commit()
            }

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, MapsFragment())
                .setReorderingAllowed(true)
                .commit()

        }
    }

    override fun navigateToDescriptionFragment(id: String) {
        supportFragmentManager.popBackStack()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, BottomSheetFragment.newInstance(id))
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToAuthorizationFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_full, AuthorizationFragment())
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToAccountFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment())
            .setReorderingAllowed(true)
            .commit()
    }

    override fun navigateToCompleteDescriptionFragment(id: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_full, DescriptionFragment.newInstance(id))
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun goBack() {
        supportFragmentManager.popBackStack()
    }

    override fun goToRouteMapActivity(coordinates: Coordinates) {
        val uri =
            "http://maps.google.com/maps?daddr=" +
                    "${coordinates.latitude}," +
                    "${coordinates.longitude}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            .setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    override fun goToPhoneCallActivity(phone: String?) {
        val uri = if (!phone.isNullOrBlank()) {
            Uri.parse("tel:$phone")
        } else {
            Uri.parse("tel:")
        }
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
    }

    override fun goToShareActivity(coordinates: Coordinates, title: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "$title\n" +
                        "Latitude: ${coordinates.latitude}\n" +
                        "Longitude: ${coordinates.longitude}"
            )
        }
        val intent = Intent.createChooser(shareIntent, "Share")
        startActivity(intent)
    }
}
