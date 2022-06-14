package com.example.onlineshop.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.model.ReviewsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CartViewModel  @Inject constructor(private val repository: Repository): ViewModel(){
    var status= MutableLiveData<ApiStatus>()
    var product= MutableLiveData<ProductsItem>()

    fun getProductById(id:Int): LiveData<ProductsItem> {
        viewModelScope.launch {
            status.value= ApiStatus.LOADING
            try{
                product.value=repository.getProductById(id)
                status.value = ApiStatus.DONE
            }
            catch(e: Exception){
                status.value = ApiStatus.ERROR
            }
        }
        return product
    }
}