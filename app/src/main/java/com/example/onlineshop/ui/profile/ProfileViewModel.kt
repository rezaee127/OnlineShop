package com.example.onlineshop.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.onlineshop.data.Repository
import com.example.onlineshop.data.errorHandling
import com.example.onlineshop.model.Address
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
            catch (e: Exception) {
                errorMessage= errorHandling(e,"profile")
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
            catch (e: Exception) {
                errorMessage= errorHandling(e)
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

    fun deleteCustomer(){
        repository.deleteCustomer(app.applicationContext)
    }


    fun getHashMapFromShared():HashMap<Int, Int>{
        return repository.getCartHashMapFromShared(app.applicationContext)
    }

    fun saveAddressListInShared( list: ArrayList<Address>?){
        repository.saveAddressListInShared(app.applicationContext,list)
    }

    fun getAddressListFromShared(): ArrayList<Address> {
        return repository.getAddressListFromShared(app.applicationContext)
    }
}

