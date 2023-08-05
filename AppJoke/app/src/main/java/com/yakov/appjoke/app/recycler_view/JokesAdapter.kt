package com.yakov.appjoke.app.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yakov.appjoke.data.model.Joke
import com.yakov.appjoke.databinding.JokeItemBinding

class JokesAdapter(private val onClick: (Joke) -> Unit) :
    RecyclerView.Adapter<JokesAdapter.JokeViewHolder>() {

    private var items = mutableListOf<Joke>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val binding = JokeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JokeViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun updateList(newItems: List<Joke>) {
        val diffResult = DiffUtil.calculateDiff(JokeDiffCallback(items, newItems))
        items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    inner class JokeViewHolder(private val binding: JokeItemBinding, val onClick: (Joke) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(joke: Joke) {
            binding.textViewJoke.text = joke.value
            binding.buttonDelete.setOnClickListener {
                onClick(joke)
            }
        }
    }

    class JokeDiffCallback(private val oldList: List<Joke>, private val newList: List<Joke>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}