package com.yakov.cinema.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.yakov.cinema.R
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.data.model.app_model.CollectionMovie
import com.yakov.cinema.databinding.ItemCollectionBinding

class CollectionAdapter(
    var onClickChoose: (CollectionMovie) -> Unit,
    var onClickDelete: (CollectionMovie) -> Unit
) :
    RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    private var listCollectionMovie: MutableList<CollectionMovie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding =
            ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionViewHolder(binding, onClickChoose, onClickDelete)
    }

    override fun getItemCount(): Int {
        return listCollectionMovie.size
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val item = listCollectionMovie[position]
        holder.bind(item)
    }

    fun update(newList: List<CollectionMovie>) {
        val moviesDiffUtil = CollectionDiffUtil(listCollectionMovie, newList)
        val check = DiffUtil.calculateDiff(moviesDiffUtil)
        listCollectionMovie.clear()
        listCollectionMovie.addAll(newList)
        check.dispatchUpdatesTo(this)
    }

    inner class CollectionViewHolder(
        var binding: ItemCollectionBinding,
        var onClickChoose: (CollectionMovie) -> Unit,
        var onClickDelete: (CollectionMovie) -> Unit,
    ) : ViewHolder(binding.root) {

        fun bind(item: CollectionMovie) {

            when (item.name) {
                Constants.COLLECTION_FAVORITE -> {
                    binding.buttonDel.visibility = View.GONE
                binding.imageViewIcon.setImageResource(R.drawable.icon_like)
                    binding.textViewName.text = Constants.COLLECTION_FAVORITE
                }

                Constants.COLLECTION_WANT_TO_SEE -> {
                    binding.buttonDel.visibility = View.GONE
                    binding.imageViewIcon.setImageResource(R.drawable.icon_not_mark)
                    binding.textViewName.text = Constants.COLLECTION_WANT_TO_SEE
                }

                else -> {
                    binding.buttonDel.visibility = View.VISIBLE
                    binding.imageViewIcon.setImageResource(R.drawable.ic_person)
                    binding.textViewName.text = item.name
                }
            }

            binding.textViewCounter.text = item.listMovie.size.toString()
            binding.buttonDel.setOnClickListener { onClickDelete(item) }
            binding.viewClick.setOnClickListener { onClickChoose(item) }

        }

    }

    class CollectionDiffUtil(
        private var oldList: List<CollectionMovie>,
        private var newList: List<CollectionMovie>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
