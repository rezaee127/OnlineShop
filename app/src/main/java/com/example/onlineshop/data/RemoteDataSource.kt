package com.example.onlineshop.data

import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.data.network.ApiService
import com.example.onlineshop.data.network.NetworkParams
import com.example.onlineshop.model.Reviews
import com.example.onlineshop.model.ReviewsItem
import retrofit2.http.Path
import retrofit2.http.QueryMap
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

    suspend fun getReviews(productId: Int): List<ReviewsItem> {
        return apiService.getReviews(productId)
    }

}