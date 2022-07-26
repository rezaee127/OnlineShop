package com.example.onlineshop.data

import android.content.Context
import com.example.onlineshop.model.Address
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.ProductsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type

const val CART_PRODUCTS_ARRAY="cartProductArray"

fun emptyCart(fileName: String,context:Context){
    val pref = context.getSharedPreferences(CART_PRODUCTS_ARRAY, Context.MODE_PRIVATE)
    pref.edit().clear().apply()
    val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    prefs.edit().clear().apply()
}

fun saveArrayOfProductToSharedPref(context: Context, list: ArrayList<ProductsItem>?) {
    val pref = context.getSharedPreferences(CART_PRODUCTS_ARRAY, Context.MODE_PRIVATE)
    val editor = pref.edit()
    val gson = Gson()
    val json: String = gson.toJson(list)
    editor.putString("cart", json)
    editor.apply()
}


fun getArrayOfProductFromSharedPref(context: Context): ArrayList<ProductsItem> {
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


fun deleteCustomerFromSharedPref(fileName: String,context:Context){
    val pref = context.getSharedPreferences("sharedCustomer", Context.MODE_PRIVATE)
    pref.edit().clear().apply()
//    val pref1 = context.getSharedPreferences("sharedAddress", Context.MODE_PRIVATE)
//    pref1.edit().clear().apply()
    val pref2 = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    pref2.edit().clear().apply()
}


fun saveAddressListToSharedPref(context: Context, list: ArrayList<Address>?) {
    val pref = context.getSharedPreferences("sharedAddress", Context.MODE_PRIVATE)
    val editor = pref.edit()
    val gson = Gson()
    val json: String = gson.toJson(list)
    editor.putString("address", json)
    editor.apply()
}


fun getAddressListFromSharedPref(context: Context): ArrayList<Address> {
    val pref = context.getSharedPreferences("sharedAddress", Context.MODE_PRIVATE)
    var arrayItems = ArrayList<Address>()
    val serializedObject = pref.getString("address", null)
    if (serializedObject != null) {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Address?>?>() {}.type
        arrayItems = gson.fromJson(serializedObject, type)
    }
    return arrayItems
}


fun errorHandling(e:Exception, fragmentName:String=""):String {
    var errorMessage = ""
    when (e) {
        is retrofit2.HttpException -> {
            errorMessage =
                "خطا در ارتباط با سرور\n\nلطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
            errorMessage = when (e.message) {
                "HTTP 400 " -> {
                    val message = if (fragmentName=="profile")
                        "$errorMessage\n\nایمیل اشتباه است یا قبلا ثبت شده است\n\n${e.message}"
                    else
                        "$errorMessage\n\nدرخواست اشتباه است\n\n${e.message}"
                    message
                }
                "HTTP 403 "->{
                    val message = if (fragmentName=="detail")
                        " این نظر وجود ندارد\n\n${e.message}"
                    else
                        "$errorMessage\n\n${e.message}"
                    message
                }

                "HTTP 401 " -> "$errorMessage\n\nاعتبار سنجی انجام نشد\n\n${e.message}"
                "HTTP 404 " -> "$errorMessage\n\nلینک اشتباه است\n\n${e.message}"
                "HTTP 500 " -> "$errorMessage\n\nارور سرور\n\n${e.message}"
                else -> "$errorMessage\n\n${e.message}"
            }
        }
        is java.lang.IllegalArgumentException -> {
            errorMessage =
                "خطا در اطلاعات ارسالی به سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید\n\n${e.message}"
        }
        is java.net.SocketTimeoutException, is java.net.UnknownHostException -> {
            errorMessage = "خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید\n\n${e.message}"
        }
        else -> {
            errorMessage = "خطا در دریافت اطلاعات\n\n${e.message}"
        }
    }
    return errorMessage
}
