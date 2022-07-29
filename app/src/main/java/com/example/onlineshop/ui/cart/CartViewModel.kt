package com.example.onlineshop.ui.cart


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.errorHandling
import com.example.onlineshop.data.repositories.CustomerRepository
import com.example.onlineshop.model.Coupon
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CartViewModel  @Inject constructor(private val customerRepository: CustomerRepository,
                                         private val app:Application): AndroidViewModel(app){

    var status= MutableLiveData<ApiStatus>()
    var listOfCoupons= MutableLiveData<List<Coupon>>()
    var errorMessage=""


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

    fun getCoupons(code:String):LiveData<List<Coupon>> {
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfCoupons.value=customerRepository.getCoupons(code)
                status.value = ApiStatus.DONE
            }
            catch(e: Exception){
                errorMessage= errorHandling(e)
                status.value = ApiStatus.ERROR
            }
        }
        return listOfCoupons
    }

}



