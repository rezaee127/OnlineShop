package com.example.onlineshop.ui.detail

import android.app.Application
import android.widget.Toast
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
class DetailViewModel @Inject constructor(private val repository: Repository, var app: Application) : ViewModel() {
    var status=MutableLiveData(ApiStatus.LOADING)
    var product= MutableLiveData<ProductsItem>()

    fun getProductById(id:Int): LiveData<ProductsItem> {
        viewModelScope.launch {
            try{
                product.value=repository.getProductById(id)
                status.value = ApiStatus.DONE
            }
            catch(e: Exception){
                Toast.makeText(app.applicationContext,"خطا در برقراری ارتباط\n لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show()
                status.value = ApiStatus.ERROR
            }
        }
        return product
    }
}