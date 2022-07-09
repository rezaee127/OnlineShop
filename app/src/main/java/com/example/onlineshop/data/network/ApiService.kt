package com.example.onlineshop.data.network

import com.example.onlineshop.model.*
import retrofit2.http.*


interface ApiService {

    @GET("coupons")
    suspend fun getCoupons(
        @Query("code")code:String,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptionsWithOutPerPage()
    ): List<Coupon>


    @GET("products")
    suspend fun getProductsOrderByDate(
        @Query("min_price")minPrice:String="1",
        @Query("orderby") orderBy: String = "date",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByPopularity(
        @Query("min_price")minPrice:String="1",
        @Query("orderby") orderBy: String = "popularity",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products")
    suspend fun getProductsOrderByRating(
        @Query("min_price")minPrice:String="1",
        @Query("orderby") orderBy: String = "rating",
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<ProductsItem>


    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptionsWithOutPerPage()
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
       // @Query("per_page") perPage: Int=30,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptionsWithOutPerPage()
    ): List<ReviewsItem>

    @GET("products/reviews/{id}")
    suspend fun getReviewById(
        @Path("id")reviewId:Int,
        @Query("product") productId: Int,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptionsWithOutPerPage()
    ): ReviewsItem

    @DELETE("products/reviews/{id}")
    suspend fun deleteReview(
        @Path("id")reviewId:Int,
        @Query("force")force:Boolean=true,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptionsWithOutPerPage()
    ): DeleteReview

    @POST("products/reviews")
    suspend fun createReview(
        @Body review: ReviewsItem,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptionsWithOutPerPage()
    ): ReviewsItem

    @PUT("products/reviews/{id}")
    suspend fun editReview(
        @Path("id")reviewId:Int,
        @Query("review")reviewText:String,
        @Query("rating")rating:Int,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptionsWithOutPerPage()
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
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptionsWithOutPerPage()
    ): CustomerItem


    @POST("orders")
    suspend fun createOrder(
        @Body orderItem: OrderItem,
        @QueryMap option: Map<String, String> = NetworkParams.getBaseOptionsWithOutPerPage()
    ): OrderItem


}