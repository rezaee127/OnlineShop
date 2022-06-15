package com.example.onlineshop.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.model.ProductsItem


class CartAdapter(var onClickItem: (Int) -> Unit,
                  var deleteProductFromCart: (Int,ProductsItem) -> Unit,
                  var plusProduct: (Int,String) -> Unit,
                  var minusProduct: (Int,String) -> Unit) :
    ListAdapter<ProductsItem, CartAdapter.ViewHolder>(ProductsItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

        val ivProduct = view.findViewById<ImageView>(R.id.iv_cart_image)
        val tvProductName = view.findViewById<TextView>(R.id.tv_cart_title)
        val tvShortDescription = view.findViewById<TextView>(R.id.tv_cart_short_description)
        val tvPrice = view.findViewById<TextView>(R.id.tv_cart_price)
        val tvCount = view.findViewById<TextView>(R.id.tv_cart_count)
        val ibAdd = view.findViewById<ImageButton>(R.id.ib_cart_add)
        val ibMinus = view.findViewById<ImageButton>(R.id.ib_cart_minus)
        val ibDelete = view.findViewById<ImageButton>(R.id.ib_cart_delete)
        var count=tvCount.text.toString().toInt()

        @SuppressLint("SetTextI18n")
        fun bind(productsItem: ProductsItem, onClickItem: (Int) -> Unit,
                 deleteProductFromCart: (Int,ProductsItem) -> Unit,
                 plusProduct: (Int,String) -> Unit,
                 minusProduct: (Int,String) -> Unit) {

            tvProductName.text = productsItem.name
            tvPrice.text="${productsItem.price} تومان"

            var str=productsItem.shortDescription
            str=str.replace("</p>","")
            str=str.replace("<p>","")
            tvShortDescription.text=str

            if (count==1)
                ibMinus.isClickable=false

            ibAdd.setOnClickListener {
                tvCount.text=(++count).toString()
                ibMinus.isClickable=true
                plusProduct(count,productsItem.price)
            }

            ibMinus.setOnClickListener {
                tvCount.text=(--count).toString()
                if (count==1)
                    ibMinus.isClickable=false
                minusProduct(count,productsItem.price)
            }
            ibDelete.setOnClickListener {
                deleteProductFromCart(count,productsItem)
            }
            tvProductName.setOnClickListener {
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
            .inflate(R.layout.cart_row_item, viewGroup, false)

        return ViewHolder(view, viewGroup.context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(getItem(position), onClickItem,deleteProductFromCart,plusProduct,minusProduct)

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