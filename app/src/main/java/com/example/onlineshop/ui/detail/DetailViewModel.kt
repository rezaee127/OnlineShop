package com.example.onlineshop.ui.detail


import android.app.Application
import androidx.lifecycle.*
import com.example.onlineshop.data.network.utils.setErrorMessage
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
                val response=productRepository.getProductById(id)
                if(response.isSuccessful){
                    product.value=response.body()
                    detailStatus.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    detailStatus.value = ApiStatus.ERROR
                }
            }
            catch(e: Exception){
                errorMessage= setErrorMessage(e)
                detailStatus.value = ApiStatus.ERROR
            }
        }
        return product
    }

    fun getReviews(productId: Int): LiveData<List<ReviewsItem>> {
        viewModelScope.launch {
            try {
                reviewsList.value = reviewRepository.getReviews(productId).body()
            } catch (e: Exception) {
            }
        }
        return reviewsList
    }

    fun getReviewById(reviewId:Int,productId: Int): LiveData<ReviewsItem> {
        viewModelScope.launch {
            try {
                receivedReview.value = reviewRepository.getReviewById(reviewId,productId).body()
            } catch (e: Exception) {
            }
        }
        return receivedReview
    }

    fun deleteReview(reviewId:Int) {
        viewModelScope.launch {
            deleteStatus.value=ApiStatus.LOADING
            try {
                val response=reviewRepository.deleteReview(reviewId)
                if(response.isSuccessful){
                    deletedReview.value=response.body()
                    deleteStatus.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    deleteStatus.value = ApiStatus.ERROR
                }
            }
            catch(e: Exception){
                errorMessage=setErrorMessage(e,0,null,"detail")
                deleteStatus.value = ApiStatus.ERROR
            }
        }
    }

    fun editReview(reviewId:Int,reviewText:String,rating:Int){
        viewModelScope.launch {
            reviewStatus.value=ApiStatus.LOADING
            try {
                val response=reviewRepository.editReview(reviewId,reviewText,rating)
                if(response.isSuccessful){
                    mReview.value=response.body()
                    reviewStatus.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    reviewStatus.value = ApiStatus.ERROR
                }
            }
            catch(e: Exception){
                errorMessage=setErrorMessage(e)
                reviewStatus.value = ApiStatus.ERROR
            }
        }
    }


    fun createReview(review: ReviewsItem): LiveData<ReviewsItem>{
        viewModelScope.launch {
            reviewStatus.value=ApiStatus.LOADING
            try {
                val response=reviewRepository.createReview(review)
                if(response.isSuccessful){
                    mReview.value=response.body()
                    reviewStatus.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    reviewStatus.value = ApiStatus.ERROR
                }
            }
            catch(e: Exception){
                errorMessage=setErrorMessage(e)
                reviewStatus.value = ApiStatus.ERROR
            }
        }
        return mReview
    }

    fun getRelatedProducts(str: String) {
        viewModelScope.launch {
            try {
                relatedProducts.value = productRepository.getRelatedProducts(str).body()
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