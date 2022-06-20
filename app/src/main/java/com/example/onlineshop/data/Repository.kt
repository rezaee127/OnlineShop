package com.example.onlineshop.data


import android.content.Context
import com.example.onlineshop.model.*
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

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


    suspend fun createCustomer(customerItem: CustomerItem): CustomerItem{
        return remoteDataSource.createCustomer(customerItem)
    }


    fun saveCustomerInShared(context: Context,customer:CustomerItem){
        saveCustomerToSharedPref(context,customer)
    }

    fun getCustomerFromShared(context: Context): CustomerItem? {
        return getCustomerFromSharedPref(context)
    }



    fun saveHashMapInShared(context: Context,hashMap: HashMap<Int, Int>){
        saveHashMapToSharedPref(context,hashMap)
    }

    fun getHashMapFromShared(context: Context): HashMap<Int, Int> {
        return getHashMapFromSharedPref(context)
    }


    fun saveArrayInShared(context: Context,list: ArrayList<ProductsItem>?){
        saveArrayToSharedPref(context,list)
    }

    fun getArrayFromShared(context: Context): ArrayList<ProductsItem> {
        return getArrayFromSharedPref(context)
    }

}