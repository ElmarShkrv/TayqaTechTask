package com.example.tayqatechtask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tayqatechtask.data.model.People
import com.example.tayqatechtask.databinding.ItemPersonsBinding

class PersonAdapter : ListAdapter<People, PersonAdapter.PersonViewHolder>(PersonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonsBinding.inflate(inflater, parent, false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        holder.bind(person)
    }

    inner class PersonViewHolder(private val binding: ItemPersonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(people: People) {
            binding.nameAndSurnameTv.text = "${people.name} ${people.surname}"
        }
    }

    private class PersonDiffCallback : DiffUtil.ItemCallback<People>() {
        override fun areItemsTheSame(oldItem: People, newItem: People): Boolean {
            return oldItem.humanId == newItem.humanId
        }

        override fun areContentsTheSame(oldItem: People, newItem: People): Boolean {
            return oldItem == newItem
        }
    }
}
