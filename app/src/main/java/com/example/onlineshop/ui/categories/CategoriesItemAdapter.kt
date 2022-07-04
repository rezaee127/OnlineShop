package com.example.onlineshop.ui.categories

import android.annotation.SuppressLint
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


class CategoriesItemAdapter(private var onClickItem: (Int, String) -> Unit) :
    ListAdapter<CategoriesItem, CategoriesItemAdapter.ViewHolder>(CategoriesItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        private val categoriesItemRow: View = view.findViewById(R.id.categories_row_item)
        private val ivCategoriesItem: ImageView = view.findViewById(R.id.iv_categories_image)
        private val tvCategoriesItemName: TextView = view.findViewById(R.id.tv_categories_name)
        private val tvCategoriesCount: TextView = view.findViewById(R.id.tv_categories_count)


        @SuppressLint("SetTextI18n")
        fun bind(categoriesItem: CategoriesItem, onClickItem: (Int, String) -> Unit) {
            tvCategoriesItemName.text = categoriesItem.name
            tvCategoriesCount.text = "${categoriesItem.count} کالا"
            categoriesItemRow.setOnClickListener {
                onClickItem(categoriesItem.id,categoriesItem.name)
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
