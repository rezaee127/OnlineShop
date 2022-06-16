package com.example.onlineshop.ui.cart


import android.content.Context
import androidx.fragment.app.Fragment
import com.example.onlineshop.model.ProductsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

const val KEY_PREF = "cart"

fun Fragment.saveArrayToShared(context: Context, key: String, list: ArrayList<ProductsItem>?) {
    val gson = Gson()
    val json: String = gson.toJson(list)
    set(context, key, json)
}

fun set(context: Context, key: String, value: String?) {
    val pref = context.getSharedPreferences("share", Context.MODE_PRIVATE)
    val editor = pref.edit()
    editor.putString(key, value)
    editor.commit()
    editor.apply()
}


//save
fun Fragment.getArrayFromShared(context: Context, key: String): ArrayList<ProductsItem> {
    val pref = context.getSharedPreferences("share", Context.MODE_PRIVATE)
    var arrayItems = ArrayList<ProductsItem>()
    val serializedObject = pref.getString(key, null)
    if (serializedObject != null) {
        val gson = Gson()
        val type: Type = object : TypeToken<List<ProductsItem?>?>() {}.type
        arrayItems = gson.fromJson(serializedObject, type)
    }
    return arrayItems
}


