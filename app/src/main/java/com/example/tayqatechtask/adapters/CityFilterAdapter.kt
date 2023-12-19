package com.example.tayqatechtask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tayqatechtask.R
import com.example.tayqatechtask.data.model.City

class CityFilterAdapter(
    private val onItemClickListener: (City) -> Unit,
) : RecyclerView.Adapter<CityFilterAdapter.ViewHolder>() {

    private val cityyList: MutableList<City> = mutableListOf()
    private val selectedItems = HashSet<City>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cityyList[position]
        holder.bind(city, selectedItems.contains(city))

        holder.itemView.setOnClickListener {
            if (selectedItems.contains(city)) {
                selectedItems.remove(city)
            } else {
                selectedItems.add(city)
            }
            notifyItemChanged(position)
            onItemClickListener.invoke(city)
        }
    }

    override fun getItemCount(): Int = cityyList.size

    fun updateCityList(newCityList: List<City>) {
        cityyList.clear()
        cityyList.addAll(newCityList)
        notifyDataSetChanged()
    }

    fun getSelectedItems(): Set<City> {
        return selectedItems
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(city: City, isSelected: Boolean) {
            itemView.findViewById<TextView>(R.id.countryName).text = city.name
            itemView.findViewById<ImageView>(R.id.selectionIcon).visibility =
                if (isSelected) View.VISIBLE else View.GONE
        }
    }
}
