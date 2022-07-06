package com.example.onlineshop.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "long")
    val long: Double,
)