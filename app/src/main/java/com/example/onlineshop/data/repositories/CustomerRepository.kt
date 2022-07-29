package com.example.onlineshop.data.repositories

import android.content.Context
import com.example.onlineshop.data.*
import com.example.onlineshop.model.*
import javax.inject.Inject

const val CART_HASHMAP="cartHashMap"
const val REVIEW_HASHMAP="reviewHashMap"

class CustomerRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun createCustomer(customerItem: CustomerItem): CustomerItem {
        return remoteDataSource.createCustomer(customerItem)
    }

    fun deleteCustomer(context: Context){
        deleteCustomerFromSharedPref(REVIEW_HASHMAP,context)
    }

    fun saveCustomerInShared(context: Context, customer: CustomerItem){
        saveCustomerToSharedPref(context,customer)
    }

    fun getCustomerFromShared(context: Context): CustomerItem? {
        return getCustomerFromSharedPref(context)
    }

    fun saveAddressListInShared(context: Context, list: ArrayList<Address>?){
        saveAddressListToSharedPref(context,list)
    }

    fun getAddressListFromShared(context: Context): ArrayList<Address> {
        return getAddressListFromSharedPref(context)
    }

    suspend fun createOrder(orderItem: OrderItem): OrderItem {
        return remoteDataSource.createOrder(orderItem)
    }


    fun saveCartHashMapInShared(context: Context, hashMap: HashMap<Int, Int>){
        saveHashMapToSharedPref(CART_HASHMAP,context,hashMap)
    }

    fun getCartHashMapFromShared(context: Context): HashMap<Int, Int> {
        return getHashMapFromSharedPref(CART_HASHMAP,context)
    }


    fun emptyShoppingCart(context: Context){
        emptyCart(CART_HASHMAP,context)
    }

    suspend fun getCoupons(code:String): List<Coupon>{
        return remoteDataSource.getCoupons(code)
    }

    fun saveArrayOfProductInShared(context: Context, list: ArrayList<ProductsItem>?){
        saveArrayOfProductToSharedPref(context,list)
    }

    fun getArrayOfProductFromShared(context: Context): ArrayList<ProductsItem> {
        return getArrayOfProductFromSharedPref(context)
    }



}