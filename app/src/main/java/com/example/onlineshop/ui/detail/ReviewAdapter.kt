package com.example.onlineshop.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.model.ReviewsItem


class ReviewAdapter() :
    ListAdapter <ReviewsItem, ReviewAdapter.ViewHolder>(ReviewsItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

        val ivReviewer = view.findViewById<ImageView>(R.id.iv_reviewer_image)
        val tvReviewerName = view.findViewById<TextView>(R.id.tv_reviewer_name)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        val tvReview = view.findViewById<TextView>(R.id.tv_review)
        val btnRateing = view.findViewById<Button>(R.id.btn_review_rating)


        fun bind(reviewsItem: ReviewsItem) {
            tvReviewerName.text=reviewsItem.reviewer
            tvReview.text=reviewsItem.review
            btnRateing.text= reviewsItem.rating.toString()

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
                    //.circleCrop()
                    .into(ivReviewer)
            } catch (e: Exception) {
                ivReviewer.setBackgroundResource(R.drawable.error)
            }

        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.reviews_row_item, viewGroup, false)

        return ViewHolder(view, viewGroup.context)
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
