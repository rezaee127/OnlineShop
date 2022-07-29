package com.example.onlineshop.data.repositories

import android.content.Context
import com.example.onlineshop.data.RemoteDataSource
import com.example.onlineshop.data.getHashMapFromSharedPref
import com.example.onlineshop.data.saveHashMapToSharedPref
import com.example.onlineshop.model.DeleteReview
import com.example.onlineshop.model.ReviewsItem
import javax.inject.Inject

class ReviewRepository @Inject constructor(private val remoteDataSource: RemoteDataSource){

    suspend fun getReviews(productId: Int): List<ReviewsItem> {
        return remoteDataSource.getReviews(productId)
    }

    suspend fun getReviewById(reviewId:Int,productId: Int): ReviewsItem {
        return remoteDataSource.getReviewById(reviewId,productId)
    }

    suspend fun deleteReview(reviewId:Int): DeleteReview {
        return remoteDataSource.deleteReview(reviewId)
    }

    suspend fun createReview(review: ReviewsItem): ReviewsItem {
        return remoteDataSource.createReview(review)
    }

    suspend fun editReview(reviewId:Int,reviewText:String,rating:Int): ReviewsItem {
        return remoteDataSource.editReview(reviewId,reviewText,rating)
    }

    fun saveReviewHashMapInShared(context: Context, hashMap: HashMap<Int, Int>){
        saveHashMapToSharedPref(REVIEW_HASHMAP,context,hashMap)
    }

    fun getReviewHashMapFromShared(context: Context): HashMap<Int, Int> {
        return getHashMapFromSharedPref(REVIEW_HASHMAP,context)
    }

}