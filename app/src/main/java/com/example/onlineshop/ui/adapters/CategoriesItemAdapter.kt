package com.example.onlineshop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.model.CategoriesItem


class CategoriesItemAdapter(var onClickItem: (Int) -> Unit) :
    ListAdapter<CategoriesItem, CategoriesItemAdapter.ViewHolder>(CategoriesItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        val categoriesItemRow = view.findViewById<View>(R.id.categories_row_item)
        val ivCategoriesItem = view.findViewById<ImageView>(R.id.iv_categories_image)
        val tvCategoriesItemName = view.findViewById<TextView>(R.id.tv_categories_name)
        val tvCategoriesCount = view.findViewById<TextView>(R.id.tv_categories_count)


        fun bind(categoriesItem: CategoriesItem, onClickItem: (Int) -> Unit) {
            tvCategoriesItemName.text = categoriesItem.name
            tvCategoriesCount.text = "${categoriesItem.count} کالا"
            categoriesItemRow.setOnClickListener {
                onClickItem(categoriesItem.id)
            }

            try {
                Glide.with(context)
                    .load(categoriesItem.image.src)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .fitCenter()
                    //.circleCrop()
                    .into(ivCategoriesItem)

            } catch (e: Exception) {
                ivCategoriesItem.setBackgroundResource(R.drawable.error)
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.categories_row_item, viewGroup, false)

        return ViewHolder(view, viewGroup.context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(getItem(position), onClickItem)

    }


    object CategoriesItemDiffCallback : DiffUtil.ItemCallback<CategoriesItem>() {
        override fun areItemsTheSame(oldItem: CategoriesItem, newItem: CategoriesItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CategoriesItem, newItem: CategoriesItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
