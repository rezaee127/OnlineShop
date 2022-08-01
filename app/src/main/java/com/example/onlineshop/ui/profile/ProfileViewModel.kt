package com.example.onlineshop.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.example.onlineshop.data.network.utils.setErrorMessage
import com.example.onlineshop.data.repositories.CustomerRepository
import com.example.onlineshop.model.Address
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.OrderItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val customerRepository: CustomerRepository,
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
                val response=customerRepository.createCustomer(customerItem)
                if(response.isSuccessful){
                    customer.value=response.body()
                    customerRequestStatus.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    customerRequestStatus.value = ApiStatus.ERROR
                }
            }
            catch (e: Exception) {
                errorMessage= setErrorMessage(e,0,null,"profile")
                customerRequestStatus.value = ApiStatus.ERROR
            }
        }
        return customer
    }



    fun createOrder(orderItem: OrderItem): LiveData<OrderItem> {

        viewModelScope.launch {
            orderRequestStatus.value = ApiStatus.LOADING
            try {
                val response=customerRepository.createOrder(orderItem)
                if(response.isSuccessful){
                    order.value=response.body()
                    orderRequestStatus.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    orderRequestStatus.value = ApiStatus.ERROR
                }
            }
            catch (e: Exception) {
                errorMessage= setErrorMessage(e)
                orderRequestStatus.value = ApiStatus.ERROR
            }
        }
        return order
    }


    fun emptyShoppingCart(){
        customerRepository.emptyShoppingCart(app.applicationContext)
    }

    fun saveCustomerInShared(customer:CustomerItem){
        customerRepository.saveCustomerInShared(app.applicationContext,customer)
    }

    fun getCustomerFromShared(): CustomerItem ?{
        return customerRepository.getCustomerFromShared(app.applicationContext)
    }

    fun deleteCustomer(){
        customerRepository.deleteCustomer(app.applicationContext)
    }


    fun getHashMapFromShared():HashMap<Int, Int>{
        return customerRepository.getCartHashMapFromShared(app.applicationContext)
    }

    fun saveAddressListInShared( list: ArrayList<Address>?){
        customerRepository.saveAddressListInShared(app.applicationContext,list)
    }

    fun getAddressListFromShared(): ArrayList<Address> {
        return customerRepository.getAddressListFromShared(app.applicationContext)
    }
}

