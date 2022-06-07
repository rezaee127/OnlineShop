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
import com.example.onlineshop.model.ProductsItem


class CategoryProductListAdapter(var onClickItem: (Int) -> Unit) :
    ListAdapter<ProductsItem, CategoryProductListAdapter.ViewHolder>(ProductsItemAdapter.ProductsItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        val productsItemRow = view.findViewById<View>(R.id.categories_row_item)
        val ivProductsItem = view.findViewById<ImageView>(R.id.iv_categories_image)
        val tvProductsItemName = view.findViewById<TextView>(R.id.tv_categories_name)
        val tvProductsPrice = view.findViewById<TextView>(R.id.tv_categories_count)



        fun bind(productsItem: ProductsItem, onClickItem: (Int) -> Unit) {
            tvProductsItemName.text = productsItem.name
            tvProductsPrice.text = "${productsItem.price} تومان"
            productsItemRow.setOnClickListener {
                onClickItem(productsItem.id)
            }

            ivProductsItem.setBackgroundResource(R.drawable.avatar_1)
            Glide.with(context)
                .load(productsItem.images[0].src)
                .placeholder(R.drawable.loading)
                .error(R.drawable.ic_baseline_error_24)
                //.override(100, 80)
                .fitCenter()
                //.circleCrop()
                .into(ivProductsItem)
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


    object ProductsItemDiffCallback : DiffUtil.ItemCallback<ProductsItem>() {
        override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
