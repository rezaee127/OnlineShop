package com.example.onlineshop.data.network

import com.example.onlineshop.model.*
import retrofit2.http.*


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

    @POST("products/reviews")
    suspend fun createReview(
        @Body review: ReviewsItem,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): ReviewsItem


    @GET("products")
    suspend fun getRelatedProducts(
        @Query("include") str: String,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products/attributes/3/terms")
    suspend fun getColorList(
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<AttributeTerm>

    @GET("products/attributes/4/terms")
    suspend fun getSizeList(
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<AttributeTerm>


    @GET("products")
    suspend fun searchProducts(
        @Query("search") searchKey: String,
        @Query("orderby") orderBy: String,
        @Query("order") order: String,
        @Query("category") category: String,
        @Query("attribute") attribute: String,
        @Query("attribute_term") attributeTerm: String,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>




    @POST("customers")
    suspend fun createCustomer(
        @Body customerItem: CustomerItem,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): CustomerItem


    @POST("orders")
    suspend fun createOrder(
        @Body orderItem: OrderItem,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): OrderItem


}