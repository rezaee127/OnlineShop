package com.example.onlineshop.data

import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.data.network.ApiService
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.ReviewsItem
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


    suspend fun getRelatedProducts(str:String): List<ProductsItem>{
        return apiService.getRelatedProducts(str)
    }


    suspend fun searchProducts(searchKey:String,orderBy: String,order: String):List<ProductsItem>{
        return apiService.searchProducts(searchKey,orderBy,order)
    }

    suspend fun createCustomer(firstName:String,lastName: String,password: String, email: String): CustomerItem{
        return apiService.createCustomer(firstName,lastName,password,email)
    }

}