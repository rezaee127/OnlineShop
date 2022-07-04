package com.example.onlineshop.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewsItem(

    @Json(name = "product_id")
    val productId: Int,
    @Json(name = "review")
    val review: String,
    @Json(name = "reviewer")
    val reviewer: String,
    @Json(name = "reviewer_email")
    val reviewerEmail: String,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "id")
    val id: Int=0,
    @Json(name = "date_created")
    val dateCreated: String="",
    @Json(name = "reviewer_avatar_urls")
    val reviewerAvatarUrls: ReviewerAvatarUrls=ReviewerAvatarUrls("","",""),
//    @Json(name = "date_created_gmt")
//    val dateCreatedGmt: String,
//    @Json(name = "_links")
//    val links: CategoriesLinks,

//    @Json(name = "status")
//    val status: String,
//    @Json(name = "verified")
//    val verified: Boolean
)