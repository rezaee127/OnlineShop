package com.example.onlineshop.data.repositories

import com.example.onlineshop.data.RemoteDataSource
import com.example.onlineshop.model.AttributeTerm
import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.model.ProductsItem
import javax.inject.Inject


class ProductRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getProductsOrderByDate(): List<ProductsItem> {
        return remoteDataSource.getProductsOrderByDate()
    }

    suspend fun getProductsOrderByPopularity(): List<ProductsItem> {
        return remoteDataSource.getProductsOrderByPopularity()
    }

    suspend fun getProductsOrderByRating(): List<ProductsItem> {
        return remoteDataSource.getProductsOrderByRating()
    }

    suspend fun getProductById(id:Int): ProductsItem {
        return remoteDataSource.getProductById(id)
    }

    suspend fun getRelatedProducts(str:String): List<ProductsItem>{
        return remoteDataSource.getRelatedProducts(str)
    }


    suspend fun getCategories(): List<CategoriesItem>{
        return  remoteDataSource.getCategories()
    }

    suspend fun getProductsListInEachCategory(category:Int): List<ProductsItem>{
        return remoteDataSource.getProductsListInEachCategory(category)
    }



    suspend fun getColorList(): List<AttributeTerm>{
        return remoteDataSource.getColorList()
    }

    suspend fun getSizeList(): List<AttributeTerm>{
        return remoteDataSource.getSizeList()
    }

    suspend fun searchProducts(searchKey:String,orderBy: String,order: String,
                               category: String,attribute: String,attributeTerm: String):List<ProductsItem>{

        return remoteDataSource.searchProducts(searchKey,orderBy,order,category,attribute,attributeTerm)
    }

}