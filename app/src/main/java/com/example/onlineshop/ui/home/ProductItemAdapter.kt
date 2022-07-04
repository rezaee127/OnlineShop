package com.example.onlineshop.ui.home

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
import com.example.onlineshop.model.ProductsItem

class ProductsItemAdapter(private var onClickItem: (Int) -> Unit) :
    ListAdapter<ProductsItem, ProductsItemAdapter.ViewHolder>(ProductsItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        private val productRow: View = view.findViewById(R.id.product_row_item)
        private val ivProduct: ImageView = view.findViewById(R.id.iv_product_image)
        private val tvProductName: TextView = view.findViewById(R.id.tv_product_name)
        private val tvPrice: TextView = view.findViewById(R.id.tv_price)


        @SuppressLint("SetTextI18n")
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
                    .error(R.drawable.error)
                    .fitCenter()
                    //.circleCrop()
                    .into(ivProduct)
            } catch (e: Exception) {
                ivProduct.setBackgroundResource(R.drawable.error)
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
