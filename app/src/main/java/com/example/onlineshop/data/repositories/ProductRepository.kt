package com.example.onlineshop.data.repositories

import com.example.onlineshop.data.RemoteDataSource
import com.example.onlineshop.model.AttributeTerm
import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.model.ProductsItem
import retrofit2.Response
import javax.inject.Inject


class ProductRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getProductsOrderByDate(): Response<List<ProductsItem>> {
        return remoteDataSource.getProductsOrderByDate()
    }

    suspend fun getProductsOrderByPopularity(): Response<List<ProductsItem>> {
        return remoteDataSource.getProductsOrderByPopularity()
    }

    suspend fun getProductsOrderByRating(): Response<List<ProductsItem>> {
        return remoteDataSource.getProductsOrderByRating()
    }

    suspend fun getProductById(id:Int): Response<ProductsItem> {
        return remoteDataSource.getProductById(id)
    }

    suspend fun getRelatedProducts(str:String) = remoteDataSource.getRelatedProducts(str)



    suspend fun getCategories(): Response<List<CategoriesItem>>{
        return  remoteDataSource.getCategories()
    }

    suspend fun getProductsListInEachCategory(category:Int)
    = remoteDataSource.getProductsListInEachCategory(category)




    suspend fun getColorList() = remoteDataSource.getColorList()


    suspend fun getSizeList() = remoteDataSource.getSizeList()


    suspend fun searchProducts(searchKey:String,orderBy: String,order: String,
                               category: String,attribute: String,attributeTerm: String)
    : Response<List<ProductsItem>>{
        return remoteDataSource.searchProducts(searchKey,orderBy,order,category,attribute,attributeTerm)
    }

}