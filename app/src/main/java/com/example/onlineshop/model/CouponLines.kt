package com.example.onlineshop.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CouponLines(
    @Json(name = "code")
    val code: String,
//    @Json(name = "id")
//    val id: Int=0,
)