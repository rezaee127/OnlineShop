package com.example.onlineshop.data.network

import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.model.ReviewsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface ApiService {

    @GET("products")
    suspend fun getProductsOrderByDate(
        @Query("orderby") orderBy: String = "date",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByPopularity(
        @Query("orderby") orderBy: String = "popularity",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByRating(
        @Query("orderby") orderBy: String = "rating",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): ProductsItem


    @GET("products/categories")
    suspend fun getCategories(
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<CategoriesItem>


    @GET("products")
    suspend fun getProductsListInEachCategory(
        @Query("category") categoryId: Int,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products/reviews")
    suspend fun getReviews(
        @Query("product") productId: Int,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ReviewsItem>

    @GET("products")
    suspend fun getRelatedProducts(
        @Query("include") str:String,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun searchProducts(
        @Query("search")searchKey:String,
        @Query("orderby")orderBy: String,
        @Query("order")order: String,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>

}
