package com.example.onlineshop.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.databinding.ProductRowItemBinding
import com.example.onlineshop.model.ProductsItem

class ProductsItemAdapter(private var onClickItem: (Int) -> Unit) :
    ListAdapter<ProductsItem, ProductsItemAdapter.ViewHolder>(ProductsItemDiffCallback) {

    inner class ViewHolder(private val binding:ProductRowItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(productsItem: ProductsItem, onClickItem: (Int) -> Unit) = binding.apply{
            tvProductName.text = productsItem.name
            if(productsItem.price!="")
                tvPrice.text="${(String.format("%,.2f", productsItem.price.toDouble())).substringBefore(".")} تومان"

            productRowItem.setOnClickListener {
                onClickItem(productsItem.id)
            }

            try {
                Glide.with(context)
                    .load(productsItem.images[0].src)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .fitCenter()
                    .into(ivProductImage)
            } catch (e: Exception) {
                ivProductImage.setBackgroundResource(R.drawable.error)
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ProductRowItemBinding = ProductRowItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context)
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
