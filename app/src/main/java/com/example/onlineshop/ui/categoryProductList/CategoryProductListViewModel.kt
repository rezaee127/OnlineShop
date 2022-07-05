package com.example.onlineshop.ui.categoryProductList


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.data.errorHandling
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CategoryProductListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    var status=MutableLiveData<ApiStatus>()
    val listOfProduct=MutableLiveData<List<ProductsItem>>()
    var errorMessage=""

    fun getProductsListInEachCategory(category:Int): LiveData<List<ProductsItem>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfProduct.value=repository.getProductsListInEachCategory(category)
                status.value = ApiStatus.DONE
            }
            catch(e:Exception){
                errorMessage= errorHandling(e)
                status.value = ApiStatus.ERROR
            }
        }
        return listOfProduct
    }
}