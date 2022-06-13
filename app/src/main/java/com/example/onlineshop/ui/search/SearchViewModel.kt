package com.example.onlineshop.ui.search

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
class SearchViewModel  @Inject constructor(private val repository: Repository): ViewModel() {
    var status= MutableLiveData(ApiStatus.LOADING)
    var listOfSearchedProduct=MutableLiveData<List<ProductsItem>>()

    fun searchProducts(searchKey:String,orderBy: String,order: String):LiveData<List<ProductsItem>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfSearchedProduct.value=repository.searchProducts(searchKey,orderBy,order)
                status.value=ApiStatus.DONE
            }
            catch (e:Exception){
                status.value=ApiStatus.ERROR
            }
        }
        return listOfSearchedProduct
    }
}