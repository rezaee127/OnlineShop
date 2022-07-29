package com.example.onlineshop.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.databinding.ReviewsRowItemBinding
import com.example.onlineshop.model.ReviewsItem


class ReviewAdapter :
    ListAdapter <ReviewsItem, ReviewAdapter.ViewHolder>(ReviewsItemDiffCallback) {

    class ViewHolder(private val binding:ReviewsRowItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reviewsItem: ReviewsItem) = binding.apply{
            tvReviewerName.text=reviewsItem.reviewer
            tvReview.text=reviewsItem.review
            btnReviewRating.text= reviewsItem.rating.toString()

            //حذف ساعت از زمان نظر دادن
            val strDate=reviewsItem.dateCreated
            tvDate.text=strDate.split('T')[0]

            //حذف کارکترهای اضافی نظرات کاربران
            var str=reviewsItem.review
            str=str.replace("br","")
            str=str.replace("p","")
            str=str.replace("< />","")
            str=str.replace("</>","")
            str=str.replace("<>","")
            tvReview.text=str


            try {
                Glide.with(context)
                    .load(reviewsItem.reviewerAvatarUrls.x24)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .fitCenter()
                    .into(ivReviewerImage)
            } catch (e: Exception) {
                ivReviewerImage.setBackgroundResource(R.drawable.error)
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReviewsRowItemBinding.inflate(LayoutInflater.from(viewGroup.context)
            , viewGroup, false)
        return ViewHolder(binding, viewGroup.context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }


    object ReviewsItemDiffCallback : DiffUtil.ItemCallback<ReviewsItem>() {
        override fun areItemsTheSame(oldItem: ReviewsItem, newItem: ReviewsItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ReviewsItem, newItem: ReviewsItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
