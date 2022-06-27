package com.example.onlineshop.ui.detail


import android.app.Application
import androidx.lifecycle.*
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.CustomerItem
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
    var errorMessage=""

    fun getProductById(id: Int): LiveData<ProductsItem> {
        viewModelScope.launch {
            detailStatus.value = ApiStatus.LOADING
            try {
                product.value = repository.getProductById(id)
                detailStatus.value = ApiStatus.DONE
            }
            catch (e:retrofit2.HttpException){
                errorMessage="خطا در ارتباط با سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                errorMessage=when(e.message){
                    "HTTP 400 "-> "$errorMessage\n\nدرخواست اشتباه است"
                    "HTTP 401 "-> "$errorMessage\n\nاعتبار سنجی انجام نشد"
                    "HTTP 404 "-> "$errorMessage\n\nلینک اشتباه است"
                    "HTTP 500 "-> "$errorMessage\n\nارور سرور"
                    else -> ""
                }
                detailStatus.value = ApiStatus.ERROR
            }
            catch (e:java.lang.IllegalArgumentException){
                errorMessage="خطا در اطلاعات ارسالی به سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                detailStatus.value = ApiStatus.ERROR
            }
            catch (e:java.net.SocketTimeoutException){
                errorMessage="خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                detailStatus.value = ApiStatus.ERROR
            }
            catch (e:java.net.UnknownHostException){
                errorMessage="خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                detailStatus.value = ApiStatus.ERROR
            }
            catch(e: java.lang.Exception){
                errorMessage="خطا در دریافت اطلاعات"
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


    fun createReview(review: ReviewsItem): LiveData<ReviewsItem>{
        viewModelScope.launch {
            reviewStatus.value=ApiStatus.LOADING
            try {
                mReview.value=repository.createReview(review)
                reviewStatus.value=ApiStatus.DONE
            }
            catch (e:retrofit2.HttpException){
                errorMessage="خطا در ارتباط با سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                errorMessage=when(e.message){
                    "HTTP 400 "-> "$errorMessage\n\nدرخواست اشتباه است"
                    "HTTP 401 "-> "$errorMessage\n\nاعتبار سنجی انجام نشد"
                    "HTTP 404 "-> "$errorMessage\n\nلینک اشتباه است"
                    "HTTP 500 "-> "$errorMessage\n\nارور سرور"
                    else -> ""
                }
                reviewStatus.value = ApiStatus.ERROR
            }
            catch (e:java.lang.IllegalArgumentException){
                errorMessage="خطا در اطلاعات ارسالی به سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                reviewStatus.value = ApiStatus.ERROR
            }
            catch (e:java.net.SocketTimeoutException){
                errorMessage="خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                reviewStatus.value = ApiStatus.ERROR
            }
            catch (e:java.net.UnknownHostException){
                errorMessage="خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                reviewStatus.value = ApiStatus.ERROR
            }
            catch(e: java.lang.Exception){
                errorMessage="خطا در دریافت اطلاعات"
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

    fun saveHashMapInShared(hashMap: HashMap<Int, Int>){
        repository.saveHashMapInShared(app.applicationContext,hashMap)
    }

    fun getHashMapFromShared():HashMap<Int, Int>{
        return repository.getHashMapFromShared(app.applicationContext)
    }

    fun getCustomerFromShared(): CustomerItem?{
        return repository.getCustomerFromShared(app.applicationContext)
    }

}