package com.example.onlineshop.data


import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.model.Reviews
import com.example.onlineshop.model.ReviewsItem
import javax.inject.Inject

class Repository @Inject constructor(/*private val localDataSource: LocalDataSource,*/ private val remoteDataSource: RemoteDataSource) {

    suspend fun getProductsOrderByDate(): List<ProductsItem> {
        return remoteDataSource.getProductsOrderByDate()
    }

    suspend fun getProductsOrderByPopularity(): List<ProductsItem> {
        return remoteDataSource.getProductsOrderByPopularity()
    }

    suspend fun getProductsOrderByRating(): List<ProductsItem> {
        return remoteDataSource.getProductsOrderByRating()
    }

    suspend fun getProductById(id:Int): ProductsItem{
        return remoteDataSource.getProductById(id)
    }

    suspend fun getCategories(): List<CategoriesItem>{
        return  remoteDataSource.getCategories()
    }

    suspend fun getProductsListInEachCategory(category:Int): List<ProductsItem>{
        return remoteDataSource.getProductsListInEachCategory(category)
    }

    suspend fun getReviews(productId: Int): List<ReviewsItem> {
        return remoteDataSource.getReviews(productId)
    }


}