package com.example.onlineshop.data


import com.example.onlineshop.model.ProductsItem
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
}