package com.example.onlineshop.ui.categories



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.network.utils.setErrorMessage
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
                val response=productRepository.getCategories()
                if(response.isSuccessful){
                    listOfCategories.value=response.body()
                    status.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    status.value = ApiStatus.ERROR
                }
            }
            catch(e: Exception){
                errorMessage= setErrorMessage(e)
                status.value = ApiStatus.ERROR
            }
        }
        return listOfCategories
    }

}