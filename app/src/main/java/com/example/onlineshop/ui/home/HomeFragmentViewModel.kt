package com.example.onlineshop.ui.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.Products
import com.example.onlineshop.model.ProductsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val repository: Repository,var app: Application): ViewModel() {
    var listOfProductsOrderByDate= MutableLiveData<List<ProductsItem>>()
    var listOfProductsOrderByPopularity= MutableLiveData<List<ProductsItem>>()
    var listOfProductsOrderByRating= MutableLiveData<List<ProductsItem>>()

    init {
        getProductsOrderByDate()
        getProductsOrderByPopularity()
        getProductsOrderByRating()
    }

    private fun getProductsOrderByDate(): LiveData<List<ProductsItem>> {
        viewModelScope.launch {
            try{
                listOfProductsOrderByDate.value=repository.getProductsOrderByDate()
            }
            catch(e:Exception){
                Toast.makeText(app.applicationContext,e.message,Toast.LENGTH_SHORT).show()
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
                Toast.makeText(app.applicationContext,e.message,Toast.LENGTH_SHORT).show()
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
                Toast.makeText(app.applicationContext,e.message,Toast.LENGTH_SHORT).show()
            }
        }
        return listOfProductsOrderByRating
    }
}