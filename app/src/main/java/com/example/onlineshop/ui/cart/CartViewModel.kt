package com.example.onlineshop.ui.cart


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.data.errorHandling
import com.example.onlineshop.model.Coupon
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CartViewModel  @Inject constructor(private val repository: Repository,
                                         private val app:Application): AndroidViewModel(app){

    var status= MutableLiveData<ApiStatus>()
    var listOfCoupons= MutableLiveData<List<Coupon>>()
    var errorMessage=""


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

    fun getCoupons(code:String):LiveData<List<Coupon>> {
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfCoupons.value=repository.getCoupons(code)
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



