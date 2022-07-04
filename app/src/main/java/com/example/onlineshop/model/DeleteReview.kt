package com.example.onlineshop.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeleteReview(
    @Json(name = "deleted")
    val deleted: Boolean,
    @Json(name = "previous")
    val previous: ReviewsItem
)