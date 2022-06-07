package com.example.onlineshop.ui.detail

import android.app.Application
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

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository, var app: Application) : ViewModel() {

    var product= MutableLiveData<ProductsItem>()

    fun getProductById(id:Int): LiveData<ProductsItem> {
        viewModelScope.launch {
            try{
                product.value=repository.getProductById(id)
            }
            catch(e: Exception){
                Toast.makeText(app.applicationContext,e.message, Toast.LENGTH_LONG).show()
            }
        }
        return product
    }
}