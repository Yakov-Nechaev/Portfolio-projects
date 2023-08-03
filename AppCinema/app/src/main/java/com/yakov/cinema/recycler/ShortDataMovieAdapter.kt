package com.yakov.cinema.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.yakov.cinema.data.model.network_model.FilmPerson
import com.yakov.cinema.databinding.ItemMovieShortDataBinding

class ShortDataMovieAdapter(private var onClick: (FilmPerson) -> Unit) :
    RecyclerView.Adapter<ShortDataMovieAdapter.ShortDataViewHolder>() {

    private var listMovie: MutableList<FilmPerson> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortDataViewHolder {
        val binding =
            ItemMovieShortDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShortDataViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    override fun onBindViewHolder(holder: ShortDataViewHolder, position: Int) {
        val item = listMovie[position]
        holder.bind(item)
    }

    fun update(newList: List<FilmPerson>) {
        val shortDataDiffUtil = ShortMovieDiffUtil(listMovie, newList)
        val check = DiffUtil.calculateDiff(shortDataDiffUtil)
        listMovie.clear()
        listMovie.addAll(newList)
        listMovie.sortBy {
            it.professionKey
        }
        check.dispatchUpdatesTo(this)
    }

    inner class ShortDataViewHolder(
        val binding: ItemMovieShortDataBinding,
        val onClick: (FilmPerson) -> Unit
    ) :
        ViewHolder(binding.root) {

        fun bind(item: FilmPerson) {
            binding.textViewNameRu.text = item.nameRu.toString()
            binding.textViewNameEn.text = item.nameEn.toString()
            binding.textViewProfessionKey.text = item.professionKey.toString()
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    class ShortMovieDiffUtil(
        private val oldList: List<FilmPerson>,
        private val newList: List<FilmPerson>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}