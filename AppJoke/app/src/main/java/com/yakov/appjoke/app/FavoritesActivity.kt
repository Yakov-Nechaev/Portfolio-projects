package com.yakov.appjoke.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakov.appjoke.app.recycler_view.JokesAdapter
import com.yakov.appjoke.app.view_model.FavoriteJokeViewModel
import com.yakov.appjoke.app.view_model.FavoriteJokeViewModelFactory
import com.yakov.appjoke.data.repository.DataRepository
import com.yakov.appjoke.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var jokeAdapter: JokesAdapter

    private val viewModel by viewModels<FavoriteJokeViewModel> {
        FavoriteJokeViewModelFactory(
            DataRepository(application as App, (application as App).jokeApi)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.fillListJoke()
        jokeAdapter = JokesAdapter { viewModel.removeJoke(it) }

        binding.saveJokeRecycler.adapter = jokeAdapter
        binding.saveJokeRecycler.layoutManager = LinearLayoutManager(this)

        viewModel.listJoke.observe(this) { jokeAdapter.updateList(it) }

        binding.buttonBack.setOnClickListener { finish() }
    }
}