package com.example.onlineshop.data

import com.example.onlineshop.data.network.ApiService
import com.example.onlineshop.model.*
import retrofit2.Response
import javax.inject.Inject


class RemoteDataSource @Inject constructor(private val apiService: ApiService){

    suspend fun getProductsOrderByDate()= apiService.getProductsOrderByDate()


    suspend fun getProductsOrderByPopularity(): Response<List<ProductsItem>>{
        return apiService.getProductsOrderByPopularity()
    }

    suspend fun getProductsOrderByRating(): Response<List<ProductsItem>>{
        return apiService.getProductsOrderByRating()
    }


    suspend fun getProductById(id:Int): Response<ProductsItem>{
        return apiService.getProductById(id)
    }

    suspend fun getCategories(): Response<List<CategoriesItem>>{
        return  apiService.getCategories()
    }

    suspend fun getProductsListInEachCategory(category:Int)
    = apiService.getProductsListInEachCategory(category)

    suspend fun getCoupons(code:String): Response<List<Coupon>> {
        return apiService.getCoupons(code)
    }

    suspend fun getReviews(productId: Int) = apiService.getReviews(productId)


    suspend fun getReviewById(reviewId:Int,productId: Int)
    = apiService.getReviewById(reviewId,productId)

    suspend fun deleteReview(reviewId:Int)= apiService.deleteReview(reviewId)



    suspend fun createReview(review: ReviewsItem) =  apiService.createReview(review)


    suspend fun editReview(reviewId:Int,reviewText:String,rating:Int)
    = apiService.editReview(reviewId,reviewText,rating)




    suspend fun getRelatedProducts(str:String) =  apiService.getRelatedProducts(str)



    suspend fun getColorList() = apiService.getColorList()


    suspend fun getSizeList() = apiService.getSizeList()



    suspend fun searchProducts(searchKey:String,orderBy: String,order: String,
                               category: String,attribute: String,attributeTerm: String)
    :Response<List<ProductsItem>>{
        return apiService.searchProducts(searchKey,orderBy,order,category,attribute,attributeTerm)
    }

    suspend fun createCustomer(customerItem: CustomerItem)= apiService.createCustomer(customerItem)


    suspend fun createOrder(orderItem: OrderItem) = apiService.createOrder(orderItem)

}