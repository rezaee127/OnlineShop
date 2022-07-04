package com.example.onlineshop.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.R
import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.ui.categories.CategoriesItemAdapter


class SearchCategoriesAdapter(private var onClickItem: (Boolean, Int) -> Unit) :
    ListAdapter<CategoriesItem, SearchCategoriesAdapter.ViewHolder>(CategoriesItemAdapter.CategoriesItemDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val chbFilterName: CheckBox = view.findViewById(R.id.chb_filter)

        fun bind(categoriesItem: CategoriesItem, onClickItem: (Boolean, Int) -> Unit) {
            chbFilterName.text=categoriesItem.name

            chbFilterName.setOnCheckedChangeListener { _, b ->
                onClickItem(b,categoriesItem.id)
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_categories_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(getItem(position),onClickItem)

    }
}