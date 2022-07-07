package com.example.onlineshop.data


import android.content.Context
import com.example.onlineshop.model.*
import javax.inject.Inject

const val CART_HASHMAP="cartHashMap"
const val REVIEW_HASHMAP="reviewHashMap"

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getProductsOrderByDate(): List<ProductsItem> {
        return remoteDataSource.getProductsOrderByDate()
    }

    suspend fun getProductsOrderByPopularity(): List<ProductsItem> {
        return remoteDataSource.getProductsOrderByPopularity()
    }

    suspend fun getProductsOrderByRating(): List<ProductsItem> {
        return remoteDataSource.getProductsOrderByRating()
    }

    suspend fun getProductById(id:Int): ProductsItem{
        return remoteDataSource.getProductById(id)
    }

    suspend fun getCategories(): List<CategoriesItem>{
        return  remoteDataSource.getCategories()
    }

    suspend fun getProductsListInEachCategory(category:Int): List<ProductsItem>{
        return remoteDataSource.getProductsListInEachCategory(category)
    }

    suspend fun getCoupons(code:String): List<Coupon>{
        return remoteDataSource.getCoupons(code)
    }

    suspend fun getReviews(productId: Int): List<ReviewsItem> {
        return remoteDataSource.getReviews(productId)
    }

    suspend fun getReviewById(reviewId:Int,productId: Int): ReviewsItem {
        return remoteDataSource.getReviewById(reviewId,productId)
    }

    suspend fun deleteReview(reviewId:Int): DeleteReview {
        return remoteDataSource.deleteReview(reviewId)
    }

    suspend fun createReview(review: ReviewsItem): ReviewsItem{
        return remoteDataSource.createReview(review)
    }

    suspend fun editReview(reviewId:Int,reviewText:String,rating:Int): ReviewsItem{
        return remoteDataSource.editReview(reviewId,reviewText,rating)
    }



    suspend fun getRelatedProducts(str:String): List<ProductsItem>{
        return remoteDataSource.getRelatedProducts(str)
    }

    suspend fun getColorList(): List<AttributeTerm>{
        return remoteDataSource.getColorList()
    }

    suspend fun getSizeList(): List<AttributeTerm>{
        return remoteDataSource.getSizeList()
    }


    suspend fun searchProducts(searchKey:String,orderBy: String,order: String,
                               category: String,attribute: String,attributeTerm: String):List<ProductsItem>{

        return remoteDataSource.searchProducts(searchKey,orderBy,order,category,attribute,attributeTerm)
    }


    suspend fun createCustomer(customerItem: CustomerItem): CustomerItem{
        return remoteDataSource.createCustomer(customerItem)
    }

    suspend fun createOrder(orderItem: OrderItem): OrderItem{
        return remoteDataSource.createOrder(orderItem)
    }

    fun emptyShoppingCart(context: Context){
        emptyCart(CART_HASHMAP,context)
    }





    fun saveCustomerInShared(context: Context,customer:CustomerItem){
        saveCustomerToSharedPref(context,customer)
    }

    fun getCustomerFromShared(context: Context): CustomerItem? {
        return getCustomerFromSharedPref(context)
    }



    fun saveCartHashMapInShared(context: Context,hashMap: HashMap<Int, Int>){
        saveHashMapToSharedPref(CART_HASHMAP,context,hashMap)
    }

    fun getCartHashMapFromShared(context: Context): HashMap<Int, Int> {
        return getHashMapFromSharedPref(CART_HASHMAP,context)
    }

    fun saveReviewHashMapInShared(context: Context,hashMap: HashMap<Int, Int>){
        saveHashMapToSharedPref(REVIEW_HASHMAP,context,hashMap)
    }

    fun getReviewHashMapFromShared(context: Context): HashMap<Int, Int> {
        return getHashMapFromSharedPref(REVIEW_HASHMAP,context)
    }



    fun saveArrayInShared(context: Context,list: ArrayList<ProductsItem>?){
        saveArrayToSharedPref(context,list)
    }

    fun getArrayFromShared(context: Context): ArrayList<ProductsItem> {
        return getArrayFromSharedPref(context)
    }

    fun saveAddressListInShared(context: Context,list: ArrayList<Address>?){
        saveAddressListToSharedPref(context,list)
    }

    fun getAddressListFromShared(context: Context): ArrayList<Address> {
        return getAddressListFromSharedPref(context)
    }

}