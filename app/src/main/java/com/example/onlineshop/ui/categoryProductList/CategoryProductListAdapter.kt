package com.example.onlineshop.ui.categoryProductList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.databinding.CategoryListRowItemBinding
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ProductsItemAdapter


class CategoryProductListAdapter(private var onClickItem: (Int) -> Unit) :
    ListAdapter<ProductsItem, CategoryProductListAdapter.ViewHolder>(ProductsItemAdapter.ProductsItemDiffCallback) {

    class ViewHolder(private val binding:CategoryListRowItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(productsItem: ProductsItem, onClickItem: (Int) -> Unit) = binding. apply{
            tvCategoryName.text = productsItem.name
            if(productsItem.price!="")
                tvCategoryCount.text = "${(String.format("%,.2f", productsItem.price.toDouble())).substringBefore(".")} تومان"
            categoryListRowItem.setOnClickListener {
                onClickItem(productsItem.id)
            }
            try{
            Glide.with(context)
                .load(productsItem.images[0].src)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .fitCenter()
                .into(ivCategoryImage)
            } catch (e: Exception) {
                ivCategoryImage.setBackgroundResource(R.drawable.error)
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryListRowItemBinding.inflate(LayoutInflater.from(viewGroup.context)
            , viewGroup, false)
        return ViewHolder(binding, viewGroup.context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position), onClickItem)
    }


}
