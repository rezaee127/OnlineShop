package com.example.onlineshop.data.repositories

import android.content.Context
import com.example.onlineshop.data.RemoteDataSource
import com.example.onlineshop.data.getHashMapFromSharedPref
import com.example.onlineshop.data.saveHashMapToSharedPref
import com.example.onlineshop.model.DeleteReview
import com.example.onlineshop.model.ReviewsItem
import javax.inject.Inject

class ReviewRepository @Inject constructor(private val remoteDataSource: RemoteDataSource){

    suspend fun getReviews(productId: Int)= remoteDataSource.getReviews(productId)

    suspend fun getReviewById(reviewId:Int,productId: Int)
    = remoteDataSource.getReviewById(reviewId,productId)


    suspend fun deleteReview(reviewId:Int) = remoteDataSource.deleteReview(reviewId)


    suspend fun createReview(review: ReviewsItem) = remoteDataSource.createReview(review)


    suspend fun editReview(reviewId:Int,reviewText:String,rating:Int)
    = remoteDataSource.editReview(reviewId,reviewText,rating)


    fun saveReviewHashMapInShared(context: Context, hashMap: HashMap<Int, Int>){
        saveHashMapToSharedPref(REVIEW_HASHMAP,context,hashMap)
    }

    fun getReviewHashMapFromShared(context: Context): HashMap<Int, Int> {
        return getHashMapFromSharedPref(REVIEW_HASHMAP,context)
    }

}