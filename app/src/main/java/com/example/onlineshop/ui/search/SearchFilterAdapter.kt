package com.example.onlineshop.ui.search


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.R
import com.example.onlineshop.model.AttributeTerm


class SearchFilterAdapter(var onClickItem: (Boolean,Int) -> Unit) :
    ListAdapter<AttributeTerm, SearchFilterAdapter.ViewHolder>(SearchFilterDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val chbFilterName: CheckBox = view.findViewById(R.id.chb_filter)

        fun bind(attributeTerm:AttributeTerm,onClickItem: (Boolean,Int) -> Unit) {
            chbFilterName.text=attributeTerm.name

            chbFilterName.setOnCheckedChangeListener { _, b ->
                onClickItem(b,attributeTerm.id)
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_filter_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(getItem(position),onClickItem)

    }


    object SearchFilterDiffCallback : DiffUtil.ItemCallback<AttributeTerm>() {
        override fun areItemsTheSame(oldItem: AttributeTerm, newItem: AttributeTerm): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AttributeTerm, newItem: AttributeTerm): Boolean {
            return oldItem.id == newItem.id
        }
    }

}