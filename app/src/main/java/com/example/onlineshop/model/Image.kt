package com.example.onlineshop.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
//    @Json(name = "alt")
//    val alt: String,
//    @Json(name = "date_created")
//    val dateCreated: String,
//    @Json(name = "date_created_gmt")
//    val dateCreatedGmt: String,
//    @Json(name = "date_modified")
//    val dateModified: String,
//    @Json(name = "date_modified_gmt")
//    val dateModifiedGmt: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "src")
    val src: String
)