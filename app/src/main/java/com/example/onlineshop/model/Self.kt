package com.example.onlineshop.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Self(
    @Json(name = "href")
    val href: String
)