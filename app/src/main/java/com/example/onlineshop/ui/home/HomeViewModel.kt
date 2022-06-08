package com.example.onlineshop.ui.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.ProductsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

enum class ApiStatus { LOADING, DONE, ERROR }

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository, var app: Application): ViewModel() {
    var listOfProductsOrderByDate= MutableLiveData<List<ProductsItem>>()
    var listOfProductsOrderByPopularity= MutableLiveData<List<ProductsItem>>()
    var listOfProductsOrderByRating= MutableLiveData<List<ProductsItem>>()
    var status=MutableLiveData(ApiStatus.LOADING)

    init {
        getProductsOrderByDate()
        getProductsOrderByPopularity()
        getProductsOrderByRating()
    }

    private fun getProductsOrderByDate(): LiveData<List<ProductsItem>> {

        viewModelScope.launch {
            try{
                listOfProductsOrderByDate.value=repository.getProductsOrderByDate()
                status.value = ApiStatus.DONE
            }
            catch(e:Exception){
                Toast.makeText(app.applicationContext,"خطا در برقراری ارتباط\n لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show()
                status.value = ApiStatus.ERROR
            }
        }
        return listOfProductsOrderByDate
    }

    private fun getProductsOrderByPopularity(): LiveData<List<ProductsItem>> {
        viewModelScope.launch {
            try{
                listOfProductsOrderByPopularity.value=repository.getProductsOrderByPopularity()
            }
            catch(e:Exception){
            }
        }
        return listOfProductsOrderByPopularity
    }

    private fun getProductsOrderByRating(): LiveData<List<ProductsItem>> {
        viewModelScope.launch {
            try{
                listOfProductsOrderByRating.value=repository.getProductsOrderByRating()
            }
            catch(e:Exception){
            }
        }
        return listOfProductsOrderByRating
    }
}