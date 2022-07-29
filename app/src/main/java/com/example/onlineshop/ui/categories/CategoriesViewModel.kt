package com.example.onlineshop.ui.categories



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.errorHandling
import com.example.onlineshop.data.repositories.ProductRepository
import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject constructor(private val productRepository: ProductRepository): ViewModel() {
    var status=MutableLiveData<ApiStatus>()
    var listOfCategories=MutableLiveData<List<CategoriesItem>>()
    var errorMessage=""

    init {
        getCategories()
    }

    fun getCategories(): LiveData<List<CategoriesItem>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfCategories.value=productRepository.getCategories()
                status.value = ApiStatus.DONE
            }
            catch(e: Exception){
                errorMessage= errorHandling(e)
                status.value = ApiStatus.ERROR
            }
        }
        return listOfCategories
    }

}