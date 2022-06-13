package com.example.onlineshop.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AttributeItem(

    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "options")
    val options: List<String>,
    @Json(name = "position")
    val position: Int,
    @Json(name = "variation")
    val variation: Boolean,
    @Json(name = "visible")
    val visible: Boolean,

//    @Json(name = "has_archives")
//    val hasArchives: Boolean,
//    @Json(name = "_links")
//    val links: Links,
//    @Json(name = "order_by")
//    val orderBy: String,
//    @Json(name = "slug")
//    val slug: String,
//    @Json(name = "type")
//    val type: String,
)