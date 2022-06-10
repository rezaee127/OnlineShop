package com.example.onlineshop.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Up(
    @Json(name = "href")
    val href: String
)