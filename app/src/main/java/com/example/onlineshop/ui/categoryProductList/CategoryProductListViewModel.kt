package com.example.onlineshop.ui.categoryProductList


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CategoryProductListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    var status=MutableLiveData<ApiStatus>()
    val listOfProduct=MutableLiveData<List<ProductsItem>>()
    var errorMessage=""

    fun getProductsListInEachCategory(category:Int): LiveData<List<ProductsItem>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfProduct.value=repository.getProductsListInEachCategory(category)
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
            catch(e:Exception){
                errorMessage="خطا در دریافت اطلاعات"
                status.value = ApiStatus.ERROR
            }
        }
        return listOfProduct
    }
}