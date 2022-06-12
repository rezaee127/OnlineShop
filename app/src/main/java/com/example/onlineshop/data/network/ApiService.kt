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
        @Query("orderby") order: String = "date",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByPopularity(
        @Query("orderby") order: String = "popularity",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByRating(
        @Query("orderby") order: String = "rating",
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

    //https://woocommerce.maktabsharif.ir/wp-json/wc/v3/products/reviews?id=666&consumer_key=ck_35f6bcc458eed45f8af8716c18772621ad139e13&consumer_secret=cs_710d145f6e04fc53ad917475459e14bcda2c9630
    @GET("products/reviews/{id}")
    suspend fun getReviews(
        @Path("id") productId: Int,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ReviewsItem>

}
