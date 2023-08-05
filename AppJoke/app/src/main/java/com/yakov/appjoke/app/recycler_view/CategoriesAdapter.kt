package com.yakov.appjoke.app.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakov.appjoke.data.model.Category
import com.yakov.appjoke.databinding.CategoryItemBinding

class CategoriesAdapter(
    private val categories: List<Category>,
    private val onClick: (Category) -> Unit
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var lastCheckedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position], position == lastCheckedPosition)
    }

    override fun getItemCount(): Int = categories.size

    inner class ViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category, isChecked: Boolean) {
            binding.radioButton.text = item.name
            binding.radioButton.isChecked = isChecked
            binding.radioButton.setOnClickListener {
                lastCheckedPosition = adapterPosition
                notifyDataSetChanged()
                onClick(item)
            }
        }
    }

    fun doSelectedItem(category: Category) {
        lastCheckedPosition = categories.indexOfFirst { it == category }
        notifyItemChanged(lastCheckedPosition)
    }
}
