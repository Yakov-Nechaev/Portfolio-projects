package com.yakov.cinema.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.yakov.cinema.data.model.app_model.AllIdMovie
import com.yakov.cinema.data.model.app_model.CollectionMovie
import com.yakov.cinema.databinding.ItemCheckboxCollectionBinding

class CheckBoxCollectionAdapter(
    var idItem: Int,
    var onClickAdd: (CollectionMovie) -> Unit,
    var onClickRemove: (CollectionMovie) -> Unit
) :
    RecyclerView.Adapter<CheckBoxCollectionAdapter.CheckBoxCollectionViewHolder>() {

    private var listCollection: MutableList<CollectionMovie> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckBoxCollectionViewHolder {
        val binding = ItemCheckboxCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CheckBoxCollectionViewHolder(binding, onClickAdd, onClickRemove)
    }

    override fun getItemCount(): Int {
        return listCollection.size
    }

    override fun onBindViewHolder(holder: CheckBoxCollectionViewHolder, position: Int) {
        val item = listCollection[position]
        holder.bind(item)
    }

    fun update(newList: List<CollectionMovie>) {
        val moviesDiffUtil = CollectionBottomDiffUtil(listCollection, newList)
        val check = DiffUtil.calculateDiff(moviesDiffUtil)
        listCollection.clear()
        listCollection.addAll(newList)
        check.dispatchUpdatesTo(this)
    }

    inner class CheckBoxCollectionViewHolder(
        var binding: ItemCheckboxCollectionBinding,
        var onClickAdd: (CollectionMovie) -> Unit,
        var onClickRemove: (CollectionMovie) -> Unit
    ) :
        ViewHolder(binding.root) {

        fun bind(item: CollectionMovie) {

            binding.textViewNameCollectionCheckBox.text = item.name
            binding.textViewCounterCollectionCheckBox.text = item.listMovie.size.toString()
            val state = item.listMovie.contains(AllIdMovie(idItem))
            binding.checkBox.isChecked = state

            binding.checkBox.setOnClickListener {
                if (state) {
                    onClickRemove(item)

                } else {
                    onClickAdd(item)
                }
            }
        }
    }

    class CollectionBottomDiffUtil(
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