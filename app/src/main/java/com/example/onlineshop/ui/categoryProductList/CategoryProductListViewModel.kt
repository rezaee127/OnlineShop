package com.example.onlineshop.ui.categoryProductList

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
class CategoryProductListViewModel @Inject constructor(private val repository: Repository, var app: Application) : ViewModel() {
    var status=MutableLiveData(ApiStatus.LOADING)
    val listOfProduct=MutableLiveData<List<ProductsItem>>()

    fun getProductsListInEachCategory(category:Int): LiveData<List<ProductsItem>>{
        viewModelScope.launch {
            try {
                listOfProduct.value=repository.getProductsListInEachCategory(category)
                status.value = ApiStatus.DONE
            }
            catch (e:Exception){
                Toast.makeText(app.applicationContext,"خطا در برقراری ارتباط\n لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show()
                status.value = ApiStatus.ERROR
            }
        }
        return listOfProduct
    }
}