package com.example.onlineshop.network


import com.example.onlineshop.model.ProductsItem
import retrofit2.http.GET
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


//    @GET("movie/popular")
//    suspend fun getPopularList(@Query("api_key")api:String=API_KEY): Popular
//
//
//    @GET("movie/upcoming")
//    suspend fun getComingSoonList(@Query("api_key")key:String= API_KEY):ComingSoon
//
//    @GET("search/movie")
//    suspend fun getSearchedMovie(
//        @Query("query")movieName:String,
//        @Query("api_key")key:String= API_KEY
//    ):Popular
//
//
//    @GET("movie/{movie_id}")
//    suspend fun getMovieDetail(
//        @Path("movie_id")movieId:Int,
//        @Query("api_key")key:String= API_KEY
//    ): Detail
//
//
//    @GET("movie/{movie_id}/videos")
//    suspend fun getVideo(
//        @Path("movie_id")id:Int,
//        @Query("api_key")key:String= API_KEY
//    ):Trailer
}
