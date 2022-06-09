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

class ProductsItemAdapter(var onClickItem: (Int) -> Unit) :
    ListAdapter<ProductsItem, ProductsItemAdapter.ViewHolder>(ProductsItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        val productRow = view.findViewById<View>(R.id.product_row_item)
        val ivProduct = view.findViewById<ImageView>(R.id.iv_product_image)
        val tvProductName = view.findViewById<TextView>(R.id.tv_product_name)
        val tvPrice = view.findViewById<TextView>(R.id.tv_price)


        fun bind(productsItem: ProductsItem, onClickItem: (Int) -> Unit) {
            tvProductName.text = productsItem.name
            tvPrice.text="${productsItem.price} تومان"
            productRow.setOnClickListener {
                onClickItem(productsItem.id)
            }

            try {
                Glide.with(context)
                    .load(productsItem.images[0].src)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .fitCenter()
                    //.circleCrop()
                    .into(ivProduct)
            } catch (e: Exception) {
                ivProduct.setBackgroundResource(R.drawable.ic_baseline_circle)
            }

        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_row_item, viewGroup, false)

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
