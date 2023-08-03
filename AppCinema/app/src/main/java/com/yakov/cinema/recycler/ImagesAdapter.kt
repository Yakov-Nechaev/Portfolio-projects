package com.yakov.cinema.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.yakov.cinema.data.model.app_model.ImagesMovie
import com.yakov.cinema.databinding.ItemImageBinding

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    private var listImages: MutableList<ImagesMovie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listImages.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = listImages[position]
        holder.bind(item)
    }

    fun update(newList: List<ImagesMovie>) {
        val imageDiffUtil = ImagesDiffUtil(listImages, newList)
        val check = DiffUtil.calculateDiff(imageDiffUtil)
        listImages.clear()
        listImages.addAll(newList)
        check.dispatchUpdatesTo(this)
    }

    inner class ImageViewHolder(var binding: ItemImageBinding) : ViewHolder(binding.root) {
        fun bind(item: ImagesMovie) {
            Glide
                .with(binding.imageItem.context)
                .load(item.urlImage)
                .into(binding.imageItem)
        }

    }

    class ImagesDiffUtil(
        private val oldList: List<ImagesMovie>,
        private val newList: List<ImagesMovie>
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