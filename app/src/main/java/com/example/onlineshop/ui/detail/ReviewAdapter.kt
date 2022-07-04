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


class ReviewAdapter :
    ListAdapter <ReviewsItem, ReviewAdapter.ViewHolder>(ReviewsItemDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

        private val ivReviewer: ImageView = view.findViewById(R.id.iv_reviewer_image)
        private val tvReviewerName: TextView = view.findViewById(R.id.tv_reviewer_name)
        private val tvDate: TextView = view.findViewById(R.id.tv_date)
        private val tvReview: TextView = view.findViewById(R.id.tv_review)
        private val btnRating: Button = view.findViewById(R.id.btn_review_rating)


        fun bind(reviewsItem: ReviewsItem) {
            tvReviewerName.text=reviewsItem.reviewer
            tvReview.text=reviewsItem.review
            btnRating.text= reviewsItem.rating.toString()

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
