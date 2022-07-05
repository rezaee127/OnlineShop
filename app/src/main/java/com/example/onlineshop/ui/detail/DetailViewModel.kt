package com.example.onlineshop.ui.detail


import android.app.Application
import androidx.lifecycle.*
import com.example.onlineshop.data.Repository
import com.example.onlineshop.data.errorHandling
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.DeleteReview
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.model.ReviewsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository,
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
                product.value = repository.getProductById(id)
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
                reviewsList.value = repository.getReviews(productId)
            } catch (e: Exception) {
            }
        }
        return reviewsList
    }

    fun getReviewById(reviewId:Int,productId: Int): LiveData<ReviewsItem> {
        viewModelScope.launch {
            try {
                receivedReview.value = repository.getReviewById(reviewId,productId)
            } catch (e: Exception) {
            }
        }
        return receivedReview
    }

    fun deleteReview(reviewId:Int) {
        viewModelScope.launch {
            deleteStatus.value=ApiStatus.LOADING
            try {
                deletedReview.value = repository.deleteReview(reviewId)
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
                mReview.value=repository.editReview(reviewId,reviewText,rating)
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
                mReview.value=repository.createReview(review)
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
        viewModelScope.async {
            try {
                relatedProducts.value = repository.getRelatedProducts(str)
            } catch (e: Exception) {
            }
        }
    }



    fun saveArrayInShared(list: ArrayList<ProductsItem>?){
        repository.saveArrayInShared(app.applicationContext,list)
    }

    fun getArrayFromShared(): ArrayList<ProductsItem>{
        return repository.getArrayFromShared(app.applicationContext)
    }

    fun saveCartHashMapInShared(hashMap: HashMap<Int, Int>){
        repository.saveCartHashMapInShared(app.applicationContext,hashMap)
    }

    fun getCartHashMapFromShared():HashMap<Int, Int>{
        return repository.getCartHashMapFromShared(app.applicationContext)
    }

    fun saveReviewHashMapInShared(hashMap: HashMap<Int, Int>){
        repository.saveReviewHashMapInShared(app.applicationContext,hashMap)
    }

    fun getReviewHashMapFromShared():HashMap<Int, Int>{
        return repository.getReviewHashMapFromShared(app.applicationContext)
    }


    fun getCustomerFromShared(): CustomerItem?{
        return repository.getCustomerFromShared(app.applicationContext)
    }

}