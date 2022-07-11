package com.example.onlineshop.data

import com.example.onlineshop.data.network.ApiService
import com.example.onlineshop.model.*
import javax.inject.Inject


class RemoteDataSource @Inject constructor(private val apiService: ApiService){

    suspend fun getProductsOrderByDate(): List<ProductsItem>{
        return apiService.getProductsOrderByDate()
    }

    suspend fun getProductsOrderByPopularity(): List<ProductsItem>{
        return apiService.getProductsOrderByPopularity()
    }

    suspend fun getProductsOrderByRating(): List<ProductsItem>{
        return apiService.getProductsOrderByRating()
    }


    suspend fun getProductById(id:Int): ProductsItem{
        return apiService.getProductById(id)
    }

    suspend fun getCategories(): List<CategoriesItem>{
        return  apiService.getCategories()
    }

    suspend fun getProductsListInEachCategory(category:Int): List<ProductsItem>{
        return apiService.getProductsListInEachCategory(category)
    }

    suspend fun getCoupons(code:String): List<Coupon>{
        return apiService.getCoupons(code)
    }

    suspend fun getReviews(productId: Int): List<ReviewsItem> {
        return apiService.getReviews(productId)
    }

    suspend fun getReviewById(reviewId:Int,productId: Int): ReviewsItem {
        return apiService.getReviewById(reviewId,productId)
    }

    suspend fun deleteReview(reviewId:Int): DeleteReview {
        return apiService.deleteReview(reviewId)
    }


    suspend fun createReview(review: ReviewsItem): ReviewsItem{
        return apiService.createReview(review)
    }

    suspend fun editReview(reviewId:Int,reviewText:String,rating:Int): ReviewsItem{
        return apiService.editReview(reviewId,reviewText,rating)
    }



    suspend fun getRelatedProducts(str:String): List<ProductsItem>{
        return apiService.getRelatedProducts(str)
    }


    suspend fun getColorList(): List<AttributeTerm>{
        return apiService.getColorList()
    }

    suspend fun getSizeList(): List<AttributeTerm>{
        return apiService.getSizeList()
    }


    suspend fun searchProducts(searchKey:String,orderBy: String,order: String,
                               category: String,attribute: String,attributeTerm: String):List<ProductsItem>{

        return apiService.searchProducts(searchKey,orderBy,order,category,attribute,attributeTerm)
    }

    suspend fun createCustomer(customerItem: CustomerItem): CustomerItem{
        return apiService.createCustomer(customerItem)
    }

    suspend fun createOrder(orderItem: OrderItem): OrderItem{
        return apiService.createOrder(orderItem)
    }
}