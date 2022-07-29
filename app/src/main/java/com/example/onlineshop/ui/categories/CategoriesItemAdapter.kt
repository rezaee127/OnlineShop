package com.example.onlineshop.ui.categories

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.databinding.CategoriesRowItemBinding
import com.example.onlineshop.model.CategoriesItem


class CategoriesItemAdapter(private var onClickItem: (Int, String) -> Unit) :
    ListAdapter<CategoriesItem, CategoriesItemAdapter.ViewHolder>(CategoriesItemDiffCallback) {

    class ViewHolder(private val binding: CategoriesRowItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(categoriesItem: CategoriesItem, onClickItem: (Int, String) -> Unit) {
            binding.apply{
                tvCategoriesName.text = categoriesItem.name
                tvCategoriesCount.text = "${categoriesItem.count} کالا"
                categoriesRowItem.setOnClickListener {
                    onClickItem(categoriesItem.id,categoriesItem.name)
                }

                try {
                    Glide.with(context)
                        .load(categoriesItem.image.src)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .fitCenter()
                        .into(ivCategoriesImage)

                } catch (e: Exception) {
                    ivCategoriesImage.setBackgroundResource(R.drawable.error)
                }
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoriesRowItemBinding.inflate(LayoutInflater.from(viewGroup.context)
            , viewGroup, false)
        return ViewHolder(binding, viewGroup.context)
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
