package com.example.onlineshop.network

import com.example.onlineshop.model.Products
import retrofit2.http.GET
import retrofit2.http.Query



private const val CONSUMER_KEY="ck_6b55bb0ff3ea0b7bf4c0aa879af50061964ce38f"
private const val CONSUMER_SECRET="cs_9b09e5125acffdd27bbe72843ced49db5f8bffb4"
const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"





interface ApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= CONSUMER_SECRET
    ): Products

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
