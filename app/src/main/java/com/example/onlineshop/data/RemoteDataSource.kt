package com.example.onlineshop.data

import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.network.ApiService
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
}