package com.example.onlineshop.data.network

import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.model.ProductsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface ApiService {

    @GET("products")
    suspend fun getProductsOrderByDate(
        @Query("orderby")order:String="date",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByPopularity(
        @Query("orderby")order:String="popularity",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByRating(
        @Query("orderby")order:String="rating",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>



    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id")id:Int,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): ProductsItem


    @GET("products/categories")
    suspend fun getCategories(
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<CategoriesItem>


    @GET("products")
    suspend fun getProductsListInEachCategory(
        @Query("category")category:Int,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ):  List<ProductsItem>


}
