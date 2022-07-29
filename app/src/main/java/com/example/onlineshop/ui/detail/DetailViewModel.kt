package com.example.onlineshop.ui.detail


import android.app.Application
import androidx.lifecycle.*
import com.example.onlineshop.data.errorHandling
import com.example.onlineshop.data.repositories.CustomerRepository
import com.example.onlineshop.data.repositories.ProductRepository
import com.example.onlineshop.data.repositories.ReviewRepository
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.DeleteReview
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.model.ReviewsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val reviewRepository: ReviewRepository,
                                          private val customerRepository: CustomerRepository,
                                          private val productRepository: ProductRepository,
                                          private val app: Application): AndroidViewModel(app) {

    var detailStatus = MutableLiveData<ApiStatus>()
    var reviewStatus = MutableLiveData<ApiStatus>()
    var product = MutableLiveData<ProductsItem>()
    var relatedProducts = MutableLiveData<List<ProductsItem>>()
    var reviewsList = MutableLiveData<List<ReviewsItem>>()
    var mReview = MutableLiveData<ReviewsItem>()
    var receivedReview = MutableLiveData<ReviewsItem>()
    var deletedReview = MutableLiveData<DeleteReview>()
    var deleteStatus = MutableLiveData<ApiStatus>()
    var errorMessage=""

    fun getProductById(id: Int): LiveData<ProductsItem> {
        viewModelScope.launch {
            detailStatus.value = ApiStatus.LOADING
            try {
                product.value = productRepository.getProductById(id)
                detailStatus.value = ApiStatus.DONE
            }
            catch(e: Exception){
                errorMessage= errorHandling(e)
                detailStatus.value = ApiStatus.ERROR
            }
        }
        return product
    }

    fun getReviews(productId: Int): LiveData<List<ReviewsItem>> {
        viewModelScope.launch {
            try {
                reviewsList.value = reviewRepository.getReviews(productId)
            } catch (e: Exception) {
            }
        }
        return reviewsList
    }

    fun getReviewById(reviewId:Int,productId: Int): LiveData<ReviewsItem> {
        viewModelScope.launch {
            try {
                receivedReview.value = reviewRepository.getReviewById(reviewId,productId)
            } catch (e: Exception) {
            }
        }
        return receivedReview
    }

    fun deleteReview(reviewId:Int) {
        viewModelScope.launch {
            deleteStatus.value=ApiStatus.LOADING
            try {
                deletedReview.value = reviewRepository.deleteReview(reviewId)
                deleteStatus.value=ApiStatus.DONE
            }
            catch(e: Exception){
                errorMessage=errorHandling(e,"detail")
                deleteStatus.value = ApiStatus.ERROR
            }
        }
    }

    fun editReview(reviewId:Int,reviewText:String,rating:Int){
        viewModelScope.launch {
            reviewStatus.value=ApiStatus.LOADING
            try {
                mReview.value=reviewRepository.editReview(reviewId,reviewText,rating)
                reviewStatus.value=ApiStatus.DONE
            }
            catch(e: Exception){
                errorMessage=errorHandling(e)
                reviewStatus.value = ApiStatus.ERROR
            }
        }
    }


    fun createReview(review: ReviewsItem): LiveData<ReviewsItem>{
        viewModelScope.launch {
            reviewStatus.value=ApiStatus.LOADING
            try {
                mReview.value=reviewRepository.createReview(review)
                reviewStatus.value=ApiStatus.DONE
            }
            catch(e: Exception){
                errorMessage=errorHandling(e)
                reviewStatus.value = ApiStatus.ERROR
            }
        }
        return mReview
    }

    fun getRelatedProducts(str: String) {
        viewModelScope.launch {
            try {
                relatedProducts.value = productRepository.getRelatedProducts(str)
            } catch (e: Exception) {
            }
        }
    }



    fun saveArrayOfProductInShared(list: ArrayList<ProductsItem>?){
        customerRepository.saveArrayOfProductInShared(app.applicationContext,list)
    }

    fun getArrayOfProductFromShared(): ArrayList<ProductsItem>{
        return customerRepository.getArrayOfProductFromShared(app.applicationContext)
    }

    fun saveCartHashMapInShared(hashMap: HashMap<Int, Int>){
        customerRepository.saveCartHashMapInShared(app.applicationContext,hashMap)
    }

    fun getCartHashMapFromShared():HashMap<Int, Int>{
        return customerRepository.getCartHashMapFromShared(app.applicationContext)
    }

    fun saveReviewHashMapInShared(hashMap: HashMap<Int, Int>){
        reviewRepository.saveReviewHashMapInShared(app.applicationContext,hashMap)
    }

    fun getReviewHashMapFromShared():HashMap<Int, Int>{
        return reviewRepository.getReviewHashMapFromShared(app.applicationContext)
    }


    fun getCustomerFromShared(): CustomerItem?{
        return customerRepository.getCustomerFromShared(app.applicationContext)
    }

}