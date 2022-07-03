package com.example.onlineshop.data

import android.content.Context
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.ProductsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

const val CART_PRODUCTS_ARRAY="cartProductArray"

fun emptyCart(fileName: String,context:Context){
    val pref = context.getSharedPreferences(CART_PRODUCTS_ARRAY, Context.MODE_PRIVATE)
    pref.edit().clear().apply()
    val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    prefs.edit().clear().apply()
}

fun saveArrayToSharedPref(context: Context, list: ArrayList<ProductsItem>?) {
    val pref = context.getSharedPreferences(CART_PRODUCTS_ARRAY, Context.MODE_PRIVATE)
    val editor = pref.edit()
    val gson = Gson()
    val json: String = gson.toJson(list)
    editor.putString("cart", json)
    editor.apply()
}


fun getArrayFromSharedPref(context: Context): ArrayList<ProductsItem> {
    val pref = context.getSharedPreferences(CART_PRODUCTS_ARRAY, Context.MODE_PRIVATE)
    var arrayItems = ArrayList<ProductsItem>()
    val serializedObject = pref.getString("cart", null)
    if (serializedObject != null) {
        val gson = Gson()
        val type: Type = object : TypeToken<List<ProductsItem?>?>() {}.type
        arrayItems = gson.fromJson(serializedObject, type)
    }
    return arrayItems
}


fun getHashMapFromSharedPref(fileName:String,context: Context): HashMap<Int, Int> {
    val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    var hashMap = HashMap<Int, Int>()
    val gson = Gson()
    val json = prefs.getString("key", null)
    if (json != null) {
        val type = object : TypeToken<HashMap<Int, Int>?>() {}.type
        hashMap = gson.fromJson(json, type)
    }
    return hashMap
}


fun saveHashMapToSharedPref(fileName:String,context: Context, obj: HashMap<Int, Int>) {
    val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    val gson = Gson()
    val json = gson.toJson(obj)
    editor.putString("key", json)
    editor.apply()
}


fun getCustomerFromSharedPref(context: Context): CustomerItem? {
    val preferences = context.getSharedPreferences("sharedCustomer", Context.MODE_PRIVATE)
    var customer: CustomerItem? = null
    val gson = Gson()
    val json = preferences.getString("customer", null)
    if (json != null) {
        val type = object : TypeToken<CustomerItem?>() {}.type
        customer = gson.fromJson(json, type)
    }
    return customer
}


fun saveCustomerToSharedPref(context: Context, obj: CustomerItem) {
    val preferences = context.getSharedPreferences("sharedCustomer", Context.MODE_PRIVATE)
    val editor = preferences.edit()
    val gson = Gson()
    val json = gson.toJson(obj)
    editor.putString("customer", json)
    editor.apply()
}

