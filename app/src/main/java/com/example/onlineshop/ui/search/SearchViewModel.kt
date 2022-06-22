package com.example.onlineshop.ui.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.AttributeTerm
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SearchViewModel  @Inject constructor(private val repository: Repository): ViewModel() {
    var status= MutableLiveData(ApiStatus.LOADING)
    var searchStatus= MutableLiveData(ApiStatus.LOADING)
    var listOfSearchedProduct=MutableLiveData<List<ProductsItem>>()
    var listOfColors=MutableLiveData<List<AttributeTerm>>()
    var listOfSizes=MutableLiveData<List<AttributeTerm>>()
    var errorMessage=""

    init {
        getColorList()
        getSizeList()
    }

    fun getColorList(): LiveData<List<AttributeTerm>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfColors.value=repository.getColorList()
                status.value=ApiStatus.DONE
            }
            catch (e:retrofit2.HttpException){
                errorMessage="خطا در ارتباط با سرور\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n در صورت تداوم مشکل با ما تماس بگیرید"
                errorMessage=when(e.message){
                    "HTTP 400 "-> "$errorMessage\nدرخواست اشتباه است"
                    "HTTP 401 "-> "$errorMessage\nاعتبار سنجی انجام نشد"
                    "HTTP 404 "-> "$errorMessage\nلینک اشتباه است"
                    "HTTP 500 "-> "$errorMessage\nارور سرور"
                    else -> ""
                }
                status.value = ApiStatus.ERROR
            }
            catch (e:java.lang.IllegalArgumentException){
                errorMessage="خطا در اطلاعات ارسالی به سرور\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n در صورت تداوم مشکل با ما تماس بگیرید"
                status.value = ApiStatus.ERROR
            }
            catch (e:java.net.SocketTimeoutException){
                errorMessage="خطا در ارتباط با اینترنت\n لطفا اتصال اینترنت خود را چک نمایید"
                status.value = ApiStatus.ERROR
            }
            catch (e:java.net.UnknownHostException){
                errorMessage="خطا در ارتباط با اینترنت\n لطفا اتصال اینترنت خود را چک نمایید"
                status.value = ApiStatus.ERROR
            }
            catch(e:Exception){
                errorMessage="خطا در دریافت اطلاعات"
                status.value = ApiStatus.ERROR
            }
        }
        return listOfColors
    }


    fun getSizeList(): LiveData<List<AttributeTerm>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfSizes.value=repository.getSizeList()
                status.value=ApiStatus.DONE
            }
            catch (e:retrofit2.HttpException){
                errorMessage="خطا در ارتباط با سرور\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n در صورت تداوم مشکل با ما تماس بگیرید"
                errorMessage=when(e.message){
                    "HTTP 400 "-> "$errorMessage\nدرخواست اشتباه است"
                    "HTTP 401 "-> "$errorMessage\nاعتبار سنجی انجام نشد"
                    "HTTP 404 "-> "$errorMessage\nلینک اشتباه است"
                    "HTTP 500 "-> "$errorMessage\nارور سرور"
                    else -> ""
                }
                status.value = ApiStatus.ERROR
            }
            catch (e:java.lang.IllegalArgumentException){
                errorMessage="خطا در اطلاعات ارسالی به سرور\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n در صورت تداوم مشکل با ما تماس بگیرید"
                status.value = ApiStatus.ERROR
            }
            catch (e:java.net.SocketTimeoutException){
                errorMessage="خطا در ارتباط با اینترنت\n لطفا اتصال اینترنت خود را چک نمایید"
                status.value = ApiStatus.ERROR
            }
            catch (e:java.net.UnknownHostException){
                errorMessage="خطا در ارتباط با اینترنت\n لطفا اتصال اینترنت خود را چک نمایید"
                status.value = ApiStatus.ERROR
            }
            catch(e:Exception){
                errorMessage="خطا در دریافت اطلاعات"
                status.value = ApiStatus.ERROR
            }
        }
        return listOfSizes
    }


    fun searchProducts(searchKey:String,orderBy: String,order: String, category: String,
                       attribute: String,attributeTerm: String):LiveData<List<ProductsItem>>{

        viewModelScope.launch {
            searchStatus.value=ApiStatus.LOADING
            try {
                listOfSearchedProduct.value=repository.searchProducts(searchKey,orderBy,order,
                    category,attribute,attributeTerm)

                searchStatus.value=ApiStatus.DONE
            }
            catch (e:retrofit2.HttpException){
                errorMessage="خطا در ارتباط با سرور\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n در صورت تداوم مشکل با ما تماس بگیرید"
                errorMessage=when(e.message){
                    "HTTP 400 "-> "$errorMessage\nدرخواست اشتباه است"
                    "HTTP 401 "-> "$errorMessage\nاعتبار سنجی انجام نشد"
                    "HTTP 404 "-> "$errorMessage\nلینک اشتباه است"
                    "HTTP 500 "-> "$errorMessage\nارور سرور"
                    else -> ""
                }
                searchStatus.value = ApiStatus.ERROR
            }
            catch (e:java.lang.IllegalArgumentException){
                errorMessage="خطا در اطلاعات ارسالی به سرور\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n در صورت تداوم مشکل با ما تماس بگیرید"
                searchStatus.value = ApiStatus.ERROR
            }
            catch (e:java.net.SocketTimeoutException){
                errorMessage="خطا در ارتباط با اینترنت\n لطفا اتصال اینترنت خود را چک نمایید"
                searchStatus.value = ApiStatus.ERROR
            }
            catch (e:java.net.UnknownHostException){
                errorMessage="خطا در ارتباط با اینترنت\n لطفا اتصال اینترنت خود را چک نمایید"
                searchStatus.value = ApiStatus.ERROR
            }
            catch(e:Exception){
                errorMessage="خطا در دریافت اطلاعات"
                searchStatus.value = ApiStatus.ERROR
            }
        }
        return listOfSearchedProduct
    }
}