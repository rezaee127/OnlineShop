package com.example.onlineshop.data


import android.content.Context
import com.example.onlineshop.model.*
import com.example.onlineshop.ui.cart.getCustomerFromSharedPref
import com.example.onlineshop.ui.cart.saveCustomerToSharedPref
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

    suspend fun getRelatedProducts(str:String): List<ProductsItem>{
        return remoteDataSource.getRelatedProducts(str)
    }

    suspend fun searchProducts(searchKey:String,orderBy: String,order: String):List<ProductsItem>{
        return remoteDataSource.searchProducts(searchKey,orderBy,order)
    }

    suspend fun createCustomer(firstName:String,lastName: String, password: String,
                               email: String): CustomerItem {
        return remoteDataSource.createCustomer(firstName,lastName,password,email)
    }

    fun saveCustomerInShared(context: Context,customer:CustomerItem){
        saveCustomerToSharedPref(context,customer)
    }

    fun getCustomerFromShared(context: Context): CustomerItem? {
        return getCustomerFromSharedPref(context)
    }

}