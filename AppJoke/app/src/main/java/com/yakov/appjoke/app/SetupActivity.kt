package com.yakov.appjoke.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.yakov.appjoke.R
import com.yakov.appjoke.app.recycler_view.CategoriesAdapter
import com.yakov.appjoke.app.view_model.SetupViewModel
import com.yakov.appjoke.app.view_model.SetupViewModelFactory
import com.yakov.appjoke.data.repository.DataRepository
import com.yakov.appjoke.databinding.ActivitySetupBinding

private const val FIFTEEN_SECONDS = 15
private const val ONE_MINUTE = 60
private const val ONE_HOUR = 3600

class SetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupBinding
    private val mainViewModel by viewModels<SetupViewModel> {
        SetupViewModelFactory(
            DataRepository(application as App, jokeApi = (application as App).jokeApi)
        )
    }
    private var radioGroupAdapter: CategoriesAdapter =
        CategoriesAdapter(emptyList()) { mainViewModel.updateJokeCategory(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) mainViewModel.hydrate()

        mainViewModel.jokeCategory.observe(this) { jokeCategory ->
            jokeCategory?.let { radioGroupAdapter.doSelectedItem(it) }
        }

        mainViewModel.timeInterval.observe(this) { interval ->
            when (interval) {
                FIFTEEN_SECONDS -> binding.sec.isChecked = true
                ONE_MINUTE -> binding.min.isChecked = true
                ONE_HOUR -> binding.hour.isChecked = true
            }
        }

        mainViewModel.allCategories.observe(this) { categories ->
            radioGroupAdapter =
                CategoriesAdapter(categories) { mainViewModel.updateJokeCategory(it) }
            binding.recyclerView.adapter = radioGroupAdapter
            binding.progressBar.visibility = View.GONE
            binding.buttonSchedule.isEnabled = true
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val interval = when (checkedId) {
                R.id.sec -> FIFTEEN_SECONDS
                R.id.min -> ONE_MINUTE
                R.id.hour -> ONE_HOUR
                else -> throw IllegalStateException("Not supported radio option checkedId")
            }
            mainViewModel.updateInterval(interval)
        }

        binding.buttonSchedule.setOnClickListener {
            mainViewModel.saveData(this)
        }

        binding.buttonSavedJokes.setOnClickListener {
            goToActivity(FavoritesActivity())
        }
    }

    private fun goToActivity(appCompatActivity: AppCompatActivity) {
        val intent = Intent(this, appCompatActivity::class.java)
        startActivity(intent)
    }
}