package com.example.countryapi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.countryapi.R
import com.example.countryapi.data.model.ListItem
import com.example.countryapi.data.model.Country

class CountryAdapter : ListAdapter<ListItem, RecyclerView.ViewHolder>(CountryDiffCallback()) {

    // have to create to viewHolders for header and country items

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_COUNTRY = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_COUNTRY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_country, parent, false)
                CountryViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ListItem.HeaderItem -> (holder as HeaderViewHolder).bind(item)
            is ListItem.CountryItem -> (holder as CountryViewHolder).bind(item.country)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListItem.HeaderItem -> VIEW_TYPE_HEADER
            is ListItem.CountryItem -> VIEW_TYPE_COUNTRY
        }
    }

    // firsst viewHolder for header items
    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNameRegion: TextView = itemView.findViewById(R.id.tvNameRegion)
        private val tvCode: TextView = itemView.findViewById(R.id.tvCode)
        private val tvCapital: TextView = itemView.findViewById(R.id.tvCapital)

        fun bind(country: Country) {
            tvNameRegion.text = "${country.name}, ${country.region}"
            tvCode.text = country.code
            tvCapital.text = country.capital
        }
    }


    //second viewHolders for country items
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvHeaderLetter: TextView = itemView.findViewById(R.id.tvHeaderLetter)

        fun bind(headerItem: ListItem.HeaderItem) {
            tvHeaderLetter.text = headerItem.letter.toString()
        }
    }


    // just for efficiency in updating the list
    class CountryDiffCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return when {
                oldItem is ListItem.HeaderItem && newItem is ListItem.HeaderItem ->
                    oldItem.letter == newItem.letter
                oldItem is ListItem.CountryItem && newItem is ListItem.CountryItem ->
                    oldItem.country.code == newItem.country.code
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem == newItem
        }
    }
}