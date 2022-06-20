package com.example.onlineshop.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LineItem(
//    @Json(name = "id")
//    val id: Int,
//    @Json(name = "meta_data")
//    val metaData: List<MetaData>,
////    @Json(name = "name")
////    val name: String,
//    @Json(name = "price")
//    val price: Int,
    @Json(name = "product_id")
    val productId: Int,
    @Json(name = "quantity")
    val quantity: Int,
//    @Json(name = "sku")
//    val sku: String,
//    @Json(name = "subtotal")
//    val subtotal: String,
//    @Json(name = "subtotal_tax")
//    val subtotalTax: String,
//    @Json(name = "tax_class")
//    val taxClass: String,
//    @Json(name = "taxes")
//    val taxes: List<Taxe>,
////    @Json(name = "total")
////    val total: String,
//    @Json(name = "total_tax")
//    val totalTax: String,
//    @Json(name = "variation_id")
//    val variationId: Int
)