package com.example.onlineshop.data.network

import com.example.onlineshop.model.*
import retrofit2.Call
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

    @GET("products")
    suspend fun getRelatedProducts(
        @Query("include") str: String,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun searchProducts(
        @Query("search") searchKey: String,
        @Query("orderby") orderBy: String,
        @Query("order") order: String,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @POST("customers")
    suspend fun createCustomer(
        @Body customerItem: CustomerItem,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): CustomerItem


    @POST("orders")
    suspend fun createOrder(
        @Query("customer_id") customerId: Int,
        @Query("line_items") lineItems: List<LineItem>,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): OrderItem


    @GET("products" )
    fun getProducts(
        @Query("search") searchText: String?,
        @Query("attribute") attribute: String?,
        @Query("attribute_term") terms: String?,
        @Query("per_page") perPage: Int,
        @Query("page") numberOfPage: Int,
        @Query("orderby") baseOn: String?,
        @Query("order") order: String?
    ):List<ProductsItem?>?

    @GET("products" )
    fun getProducts(
        @Query("search") searchText: String?,
        @Query("attribute") attribute: String?,
        @Query("attribute_term") terms: String?,
        @Query("per_page") perPage: Int,
        @Query("page") numberOfPage: Int
    ): List<ProductsItem?>?


    @GET("products")
    fun getProducts(
        @Query("attribute") attribute: String?,
        @Query("attribute_term") terms: String?,
        @Query("per_page") perPage: Int,
        @Query("page") numberOfPage: Int
    ): List<ProductsItem?>?

    @GET("products" )
    fun searchProducts(
        @Query("search") searchText: String?,
        @Query("per_page") perPage: Int,
        @Query("page") numberOfPage: Int
    ): List<ProductsItem?>?


}