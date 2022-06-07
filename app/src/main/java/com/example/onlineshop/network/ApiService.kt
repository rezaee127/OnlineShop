package com.example.onlineshop.network


import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.model.ProductsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query



private const val CONSUMER_KEY="ck_35f6bcc458eed45f8af8716c18772621ad139e13"
private const val CONSUMER_SECRET="cs_710d145f6e04fc53ad917475459e14bcda2c9630"
const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"





interface ApiService {

    @GET("products")
    suspend fun getProductsOrderByDate(
        @Query("per_page")perPage:Int=20,
        @Query("orderby")order:String="date",
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= CONSUMER_SECRET
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByPopularity(
        @Query("per_page")perPage:Int=20,
        @Query("orderby")order:String="popularity",
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= CONSUMER_SECRET
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByRating(
        @Query("per_page")perPage:Int=20,
        @Query("orderby")order:String="rating",
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= CONSUMER_SECRET
    ): List<ProductsItem>



    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id")id:Int,
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= CONSUMER_SECRET
    ): ProductsItem


    @GET("products/categories")
    suspend fun getCategories(
        @Query("per_page")perPage:Int=30,
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= CONSUMER_SECRET
    ): List<CategoriesItem>


    @GET("products")
    suspend fun getProductsListInEachCategory(
        @Query("category")category:Int,
        @Query("per_page")perPage:Int=30,
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= CONSUMER_SECRET
    ):  List<ProductsItem>


}
