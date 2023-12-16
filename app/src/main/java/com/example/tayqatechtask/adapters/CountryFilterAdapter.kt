package com.example.tayqatechtask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tayqatechtask.R
import com.example.tayqatechtask.data.model.Country

class CountryFilterAdapter(
    private val countryList: List<Country>,
    private val onItemClickListener: (Set<Country>) -> Unit
) : RecyclerView.Adapter<CountryFilterAdapter.ViewHolder>() {

    private val selectedItems = HashSet<Country>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countryList[position]
        holder.bind(country, selectedItems.contains(country))

        holder.itemView.setOnClickListener {
            if (selectedItems.contains(country)) {
                selectedItems.remove(country)
            } else {
                selectedItems.add(country)
            }
            notifyItemChanged(position)
            onItemClickListener.invoke(selectedItems)
        }

//        holder.itemView.setOnClickListener {
//            if (selectedItems.contains(country)) {
//                selectedItems.remove(country)
//            } else {
//                selectedItems.add(country)
//            }
//            notifyItemChanged(position)
//            onItemClickListener.invoke(country)
//        }
    }

    override fun getItemCount(): Int = countryList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(country: Country, isSelected: Boolean) {
            itemView.findViewById<TextView>(R.id.countryName).text = country.name
            itemView.findViewById<ImageView>(R.id.selectionIcon).visibility =
                if (isSelected) View.VISIBLE else View.GONE
        }
    }

    fun getSelectedItems(): Set<Country> {
        return selectedItems
    }

}