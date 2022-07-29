package com.example.onlineshop.ui.cart

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


class CartAdapter(
    private var productMap:HashMap<Int,Int>, private var onClickItem: (Int) -> Unit,
    private var deleteProductFromCart: (ProductsItem) -> Unit,
    private var changeProductCount: (operator:String, Int, ProductsItem) -> Unit ) :
    ListAdapter<ProductsItem, CartAdapter.ViewHolder>(ProductsItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

        private val ivProduct: ImageView = view.findViewById(R.id.iv_cart_image)
        private val tvProductName: TextView = view.findViewById(R.id.tv_cart_title)
        private val tvShortDescription: TextView = view.findViewById(R.id.tv_cart_short_description)
        private val tvPrice: TextView = view.findViewById(R.id.tv_cart_price)
        private val tvCount: TextView = view.findViewById(R.id.tv_cart_count)
        private val ibAdd: ImageButton = view.findViewById(R.id.ib_cart_add)
        private val ibMinus: ImageButton = view.findViewById(R.id.ib_cart_minus)
        private val ibDelete: ImageButton = view.findViewById(R.id.ib_cart_delete)

        @SuppressLint("SetTextI18n")
        fun bind(productsItem: ProductsItem, productMap: HashMap<Int, Int>,
                 onClickItem: (Int) -> Unit, deleteProductFromCart: (ProductsItem) -> Unit,
                 changeProductCount: (operator:String,Int,ProductsItem) -> Unit)
        {
            var count= productMap[productsItem.id]!!
            tvCount.text= productMap[productsItem.id].toString()
            tvProductName.text = productsItem.name
            if(productsItem.price!="")
                tvPrice.text="${(String.format("%,.2f", productsItem.price.toDouble())).substringBefore(".")} تومان"

            var str=productsItem.shortDescription
            str=str.replace("</p>","")
            str=str.replace("<p>","")
            tvShortDescription.text=str

            if (count<=1)
                ibMinus.isClickable=false

            ibAdd.setOnClickListener {
                tvCount.text=(++count).toString()
                ibMinus.isClickable=true
                changeProductCount("+",count,productsItem)
            }

            ibMinus.setOnClickListener {
                if (count<=1){
                    ibMinus.isClickable=false
                }else{
                    tvCount.text=(--count).toString()
                    changeProductCount("-",count,productsItem)
                }
            }
            ibDelete.setOnClickListener {
                deleteProductFromCart(productsItem)
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

        viewHolder.bind(getItem(position),productMap, onClickItem,deleteProductFromCart,changeProductCount)

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