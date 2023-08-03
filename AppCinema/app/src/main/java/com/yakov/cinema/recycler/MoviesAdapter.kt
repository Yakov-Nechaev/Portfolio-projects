package com.yakov.cinema.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.databinding.ItemMovieBinding

class MoviesAdapter(private val onClick: (BriefMovie) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var listMovies: MutableList<BriefMovie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int {
        return listMovies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = listMovies[position]
        holder.bind(item)
    }

    fun update(newList: List<BriefMovie>) {
        val moviesDiffUtil = MoviesDiffUtil(listMovies, newList)
        val check = DiffUtil.calculateDiff(moviesDiffUtil)
        listMovies.clear()
        listMovies.addAll(newList)
        check.dispatchUpdatesTo(this)
    }

    fun updateWithLastClean(newList: List<BriefMovie>) {
        val moviesDiffUtil = MoviesDiffUtil(listMovies, newList)
        val check = DiffUtil.calculateDiff(moviesDiffUtil)
        listMovies.clear()
        listMovies.addAll(newList)
        listMovies.add(BriefMovie(id = -100, image = "", name = "", rating = "", genre = ""))
        check.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun hardUpdate() {
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(var binding: ItemMovieBinding, var onClick: (BriefMovie) -> Unit) :
        ViewHolder(binding.root) {

        fun bind(item: BriefMovie) {
            if (item.id == -100) {
                binding.apply {
                    imageViewPoster.visibility = View.GONE
                    textViewRating.visibility = View.GONE
                    textViewTitle.visibility = View.GONE
                    textViewGenre.visibility = View.GONE
                    cardView.visibility = View.GONE

                    clearHistoryButton.visibility = View.VISIBLE
                }
            } else {

                binding.apply {
                    imageViewPoster.visibility = View.VISIBLE
                    textViewRating.visibility = View.VISIBLE
                    textViewTitle.visibility = View.VISIBLE
                    textViewGenre.visibility = View.VISIBLE
                    cardView.visibility = View.VISIBLE

                    clearHistoryButton.visibility = View.GONE
                }

                binding.textViewTitle.text = item.name

                binding.textViewGenre.apply {
                    if (!item.genre.isNullOrEmpty()) {
                        text = item.genre
                    } else {
                        visibility = View.GONE
                    }
                }

                binding.textViewRating.apply {
                    if (!item.rating.isNullOrEmpty()) {
                        text = item.rating
                    } else {
                        visibility = View.GONE
                    }
                }
                Glide
                    .with(binding.imageViewPoster.context)
                    .load(item.image)
                    .into(binding.imageViewPoster)
            }

            binding.root.setOnClickListener {
                onClick(item)
            }
        }

    }

    class MoviesDiffUtil(
        private var oldList: List<BriefMovie>,
        private var newList: List<BriefMovie>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].image == newList[newItemPosition].image
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}



