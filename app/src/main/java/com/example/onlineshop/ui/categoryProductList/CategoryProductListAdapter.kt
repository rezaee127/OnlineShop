package com.example.onlineshop.ui.categoryProductList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ProductsItemAdapter


class CategoryProductListAdapter(private var onClickItem: (Int) -> Unit) :
    ListAdapter<ProductsItem, CategoryProductListAdapter.ViewHolder>(ProductsItemAdapter.ProductsItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        private val productsItemRow: View = view.findViewById(R.id.category_list_row_item)
        private val ivProductsItem: ImageView = view.findViewById(R.id.iv_category_image)
        private val tvProductsItemName: TextView = view.findViewById(R.id.tv_category_name)
        private val tvProductsPrice: TextView = view.findViewById(R.id.tv_category_count)



        @SuppressLint("SetTextI18n")
        fun bind(productsItem: ProductsItem, onClickItem: (Int) -> Unit) {
            tvProductsItemName.text = productsItem.name
            tvProductsPrice.text = "${productsItem.price} تومان"
            productsItemRow.setOnClickListener {
                onClickItem(productsItem.id)
            }
            try{
            Glide.with(context)
                .load(productsItem.images[0].src)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .fitCenter()
                //.circleCrop()
                .into(ivProductsItem)
            } catch (e: Exception) {
                ivProductsItem.setBackgroundResource(R.drawable.error)
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.category_list_row_item, viewGroup, false)

        return ViewHolder(view, viewGroup.context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(getItem(position), onClickItem)

    }


}
