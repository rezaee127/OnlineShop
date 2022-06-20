package com.example.onlineshop.ui.detail


import android.app.Application
import androidx.lifecycle.*
import com.example.onlineshop.data.Repository
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

    var status = MutableLiveData<ApiStatus>()
    var product = MutableLiveData<ProductsItem>()
    var relatedProducts = MutableLiveData<List<ProductsItem>>()
    var reviewsList = MutableLiveData<List<ReviewsItem>>()
    var errorMessage=""

    fun getProductById(id: Int): LiveData<ProductsItem> {
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                product.value = repository.getProductById(id)
                status.value = ApiStatus.DONE
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
                status.value = ApiStatus.ERROR
            }
            catch (e:java.lang.IllegalArgumentException){
                errorMessage="خطا در اطلاعات ارسالی به سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                status.value = ApiStatus.ERROR
            }
            catch (e:java.net.SocketTimeoutException){
                errorMessage="خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                status.value = ApiStatus.ERROR
            }
            catch (e:java.net.UnknownHostException){
                errorMessage="خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                status.value = ApiStatus.ERROR
            }
            catch(e: java.lang.Exception){
                errorMessage="خطا در دریافت اطلاعات"
                status.value = ApiStatus.ERROR
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
}