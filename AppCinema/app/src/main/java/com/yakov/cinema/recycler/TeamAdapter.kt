package com.yakov.cinema.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.yakov.cinema.data.model.app_model.Actor
import com.yakov.cinema.data.model.app_model.Personnel
import com.yakov.cinema.data.model.app_model.Team
import com.yakov.cinema.databinding.ItemParticipantBinding
import kotlin.IllegalArgumentException

class TeamAdapter(private var onClick: (item: Team) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    private var teamMovie: MutableList<Team> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            ACTOR_VIEW_TYPE -> {
                val binding = ItemParticipantBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ActorViewHolder(binding, onClick)
            }

            PERSONNEL_VIEW_TYPE -> {
                val binding = ItemParticipantBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return PersonnelViewHolder(binding, onClick)
            }

            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return teamMovie.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       when (holder.itemViewType) {
           ACTOR_VIEW_TYPE -> {
               val item = teamMovie[position] as Actor
               val viewHolder = holder as ActorViewHolder
               viewHolder.bind(item)
           }
           PERSONNEL_VIEW_TYPE -> {
               val item = teamMovie[position] as Personnel
               val viewHolder = holder as PersonnelViewHolder
               viewHolder.bind(item)
           }
       }
    }

    override fun getItemViewType(position: Int): Int {
        return when (teamMovie[position]) {
            is Actor -> ACTOR_VIEW_TYPE
            is Personnel -> PERSONNEL_VIEW_TYPE
            else -> throw IllegalArgumentException("Unknown list type")
        }

    }

    fun update(newList: List<Team>) {
        val teamDiffUtil = TeamDiffUtil(teamMovie, newList)
        val check = DiffUtil.calculateDiff(teamDiffUtil)
        teamMovie.clear()
        teamMovie.addAll(newList)
        check.dispatchUpdatesTo(this)
    }

    inner class ActorViewHolder(
        private val binding: ItemParticipantBinding,
        private var onClick: (item: Team) -> Unit
    ) :
        ViewHolder(binding.root) {

        fun bind(item: Actor) {
            binding.textViewParticipantName.text = item.name
            binding.textViewParticipantRole.text = item.role
            Glide
                .with(binding.imageViewParticipantImage.context)
                .load(item.image)
                .into(binding.imageViewParticipantImage)
            binding.root.setOnClickListener {
                onClick(item)
            }
        }

    }

    inner class PersonnelViewHolder(
        private val binding: ItemParticipantBinding,
        private var onClick: (item: Team) -> Unit
    ) :
        ViewHolder(binding.root) {
        fun bind(item: Personnel) {
            binding.textViewParticipantName.text = item.name
            binding.textViewParticipantRole.text = item.role
            Glide
                .with(binding.imageViewParticipantImage.context)
                .load(item.image)
                .into(binding.imageViewParticipantImage)
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    class TeamDiffUtil(
        private var oldList: List<Team>,
        private var newList: List<Team>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id() == newList[newItemPosition].id()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    companion object {
        const val ACTOR_VIEW_TYPE = 0
        const val PERSONNEL_VIEW_TYPE = 1
    }
}