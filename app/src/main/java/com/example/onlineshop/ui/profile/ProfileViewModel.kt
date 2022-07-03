package com.example.onlineshop.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.OrderItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: Repository,
                                           private val app:Application): AndroidViewModel(app) {

    var customer=MutableLiveData<CustomerItem>()
    var customerRequestStatus= MutableLiveData<ApiStatus>()

    var order=MutableLiveData<OrderItem>()
    var orderRequestStatus= MutableLiveData<ApiStatus>()
    var errorMessage=""

    fun createCustomer(customerItem: CustomerItem): LiveData<CustomerItem> {

        viewModelScope.launch {
            customerRequestStatus.value = ApiStatus.LOADING
            try {
                customer.value = repository.createCustomer(customerItem)
                customerRequestStatus.value = ApiStatus.DONE
            }
            catch (e: retrofit2.HttpException) {
                errorMessage = "خطا در ارتباط با سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                errorMessage = when (e.message) {
                    "HTTP 400 " -> "ایمیل اشتباه است یا قبلا ثبت شده است\n\n لطفا با یک ایمیل دیگر، ثبت نام نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                    "HTTP 401 " -> "$errorMessage\n\nاعتبار سنجی انجام نشد"
                    "HTTP 404 " -> "$errorMessage\n\nلینک اشتباه است"
                    "HTTP 500 " -> "$errorMessage\n\nارور سرور"
                    else -> ""
                }
                customerRequestStatus.value = ApiStatus.ERROR
            } catch (e: java.lang.IllegalArgumentException) {
                errorMessage = "خطا در اطلاعات ارسالی به سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                customerRequestStatus.value = ApiStatus.ERROR
            } catch (e: java.net.SocketTimeoutException) {
                errorMessage = "خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                customerRequestStatus.value = ApiStatus.ERROR
            } catch (e: java.net.UnknownHostException) {
                errorMessage = "خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                customerRequestStatus.value = ApiStatus.ERROR
            } catch (e: Exception) {
                errorMessage = "خطا در دریافت اطلاعات"
                customerRequestStatus.value = ApiStatus.ERROR
            }
        }
        return customer
    }



    fun createOrder(orderItem: OrderItem): LiveData<OrderItem> {

        viewModelScope.launch {
            orderRequestStatus.value = ApiStatus.LOADING
            try {
                order.value = repository.createOrder(orderItem)
                orderRequestStatus.value = ApiStatus.DONE
            }
            catch (e: retrofit2.HttpException) {
                errorMessage = "خطا در ارتباط با سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                errorMessage = when (e.message) {
                    "HTTP 400 "-> "$errorMessage\n\nدرخواست اشتباه است"
                    "HTTP 401 "-> "$errorMessage\n\nاعتبار سنجی انجام نشد"
                    "HTTP 404 "-> "$errorMessage\n\nلینک اشتباه است"
                    "HTTP 500 "-> "$errorMessage\n\nارور سرور"
                    else -> ""
                }
                orderRequestStatus.value = ApiStatus.ERROR
            } catch (e: java.lang.IllegalArgumentException) {
                errorMessage = "خطا در اطلاعات ارسالی به سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
                orderRequestStatus.value = ApiStatus.ERROR
            } catch (e: java.net.SocketTimeoutException) {
                errorMessage = "خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                orderRequestStatus.value = ApiStatus.ERROR
            } catch (e: java.net.UnknownHostException) {
                errorMessage = "خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید"
                orderRequestStatus.value = ApiStatus.ERROR
            } catch (e: Exception) {
                errorMessage = "خطا در دریافت اطلاعات"
                orderRequestStatus.value = ApiStatus.ERROR
            }
        }
        return order
    }


    fun emptyShoppingCart(){
        repository.emptyShoppingCart(app.applicationContext)
    }

    fun saveCustomerInShared(customer:CustomerItem){
        repository.saveCustomerInShared(app.applicationContext,customer)
    }

    fun getCustomerFromShared(): CustomerItem ?{
        return repository.getCustomerFromShared(app.applicationContext)
    }


    fun getHashMapFromShared():HashMap<Int, Int>{
        return repository.getCartHashMapFromShared(app.applicationContext)
    }
}

