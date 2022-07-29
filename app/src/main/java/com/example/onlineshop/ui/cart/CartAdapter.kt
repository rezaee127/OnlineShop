package com.example.onlineshop.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.databinding.CartRowItemBinding
import com.example.onlineshop.model.ProductsItem


class CartAdapter(
    private var productMap:HashMap<Int,Int>, private var onClickItem: (Int) -> Unit,
    private var deleteProductFromCart: (ProductsItem) -> Unit,
    private var changeProductCount: (operator:String, Int, ProductsItem) -> Unit ) :
    ListAdapter<ProductsItem, CartAdapter.ViewHolder>(ProductsItemDiffCallback) {

    class ViewHolder(private val binding:CartRowItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(productsItem: ProductsItem, productMap: HashMap<Int, Int>,
                 onClickItem: (Int) -> Unit, deleteProductFromCart: (ProductsItem) -> Unit,
                 changeProductCount: (operator:String,Int,ProductsItem) -> Unit)
        {
            binding.apply {
                var count= productMap[productsItem.id]!!
                tvCartCount.text= productMap[productsItem.id].toString()
                tvCartTitle.text = productsItem.name
                if(productsItem.price!="")
                    tvCartPrice.text="${(String.format("%,.2f", productsItem.price.toDouble())).substringBefore(".")} تومان"

                var str=productsItem.shortDescription
                str=str.replace("</p>","")
                str=str.replace("<p>","")
                tvCartShortDescription.text=str

                if (count<=1)
                    ibCartMinus.isClickable=false

                ibCartAdd.setOnClickListener {
                    tvCartCount.text=(++count).toString()
                    ibCartMinus.isClickable=true
                    changeProductCount("+",count,productsItem)
                }

                ibCartMinus.setOnClickListener {
                    if (count<=1){
                        ibCartMinus.isClickable=false
                    }else{
                        tvCartCount.text=(--count).toString()
                        changeProductCount("-",count,productsItem)
                    }
                }
                ibCartDelete.setOnClickListener {
                    deleteProductFromCart(productsItem)
                }
                tvCartTitle.setOnClickListener {
                    onClickItem(productsItem.id)
                }

                try {
                    Glide.with(context)
                        .load(productsItem.images[0].src)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .fitCenter()
                        .into(ivCartImage)
                } catch (e: Exception) {
                    ivCartImage.setBackgroundResource(R.drawable.error)
                }
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartRowItemBinding.inflate(LayoutInflater.from(viewGroup.context),
            viewGroup, false)

        return ViewHolder(binding, viewGroup.context)
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