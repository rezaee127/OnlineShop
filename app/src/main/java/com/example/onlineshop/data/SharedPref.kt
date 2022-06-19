package com.example.onlineshop.ui.cart


import android.content.Context
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.ProductsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


const val KEY_PREF = "cart"

fun Fragment.saveArrayToSharedPref(context: Context, key: String, list: ArrayList<ProductsItem>?) {
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
fun Fragment.getArrayFromSharedPref(context: Context, key: String): ArrayList<ProductsItem> {
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


fun Fragment.getHashMapFromSharedPref(context: Context): HashMap<Int, Int> {
    val prefs = context.getSharedPreferences("shares", Context.MODE_PRIVATE)
    var hashMap=HashMap<Int, Int>()
    val gson = Gson()
    val json = prefs.getString("key", null)
    if(json!=null){
        val type = object : TypeToken<HashMap<Int, Int>?>() {}.type
        hashMap= gson.fromJson(json, type)
    }
    return hashMap
}


fun Fragment.saveHashMapToSharedPref(context: Context, obj: HashMap<Int, Int>) {
    val prefs = context.getSharedPreferences("shares", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    val gson = Gson()
    val json = gson.toJson(obj)
    editor.putString("key", json)
    editor.apply()
}



fun getCustomerFromSharedPref(context: Context): CustomerItem? {
    val preferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
    var customer:CustomerItem?=null
    val gson = Gson()
    val json = preferences.getString("customer", null)
    if(json!=null){
        val type = object : TypeToken<CustomerItem?>() {}.type
        customer= gson.fromJson(json, type)
    }
    return customer
}


fun saveCustomerToSharedPref(context: Context, obj: CustomerItem) {
    val preferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
    val editor = preferences.edit()
    val gson = Gson()
    val json = gson.toJson(obj)
    editor.putString("customer", json)
    editor.apply()
}










