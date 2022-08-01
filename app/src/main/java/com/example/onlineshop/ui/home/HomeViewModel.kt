package com.example.onlineshop.ui.home



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.network.utils.setErrorMessage
import com.example.onlineshop.data.repositories.ProductRepository
import com.example.onlineshop.model.ProductsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

enum class ApiStatus { LOADING, DONE, ERROR }

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository): ViewModel() {
    var listOfProductsOrderByDate= MutableLiveData<List<ProductsItem>>()
    var listOfProductsOrderByPopularity= MutableLiveData<List<ProductsItem>>()
    var listOfProductsOrderByRating= MutableLiveData<List<ProductsItem>>()
    var status=MutableLiveData<ApiStatus>()
    var errorMessage=""
    var specialProducts=MutableLiveData<ProductsItem>()

    init {
        getSpecialProduct()
        getProductsOrderByDate()
        getProductsOrderByPopularity()
        getProductsOrderByRating()
    }

    fun getProductsOrderByDate(): LiveData<List<ProductsItem>> {

        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try{
                val response=productRepository.getProductsOrderByDate()
                if(response.isSuccessful){
                    listOfProductsOrderByDate.value=response.body()
                    status.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    status.value = ApiStatus.ERROR
                }
            }
            catch(e:Exception){
                errorMessage= setErrorMessage(e)
                status.value = ApiStatus.ERROR
            }
        }
        return listOfProductsOrderByDate
    }

    fun getProductsOrderByPopularity(): LiveData<List<ProductsItem>> {
        viewModelScope.launch {
            try{
                listOfProductsOrderByPopularity.value=productRepository.getProductsOrderByPopularity().body()
            }
            catch(e:Exception){
            }
        }
        return listOfProductsOrderByPopularity
    }

    fun getProductsOrderByRating(): LiveData<List<ProductsItem>> {
        viewModelScope.launch {
            try{
                listOfProductsOrderByRating.value=productRepository.getProductsOrderByRating().body()
            }
            catch(e:Exception){
            }
        }
        return listOfProductsOrderByRating
    }



    fun getSpecialProduct(): LiveData<ProductsItem> {
        viewModelScope.launch {
            try {
                specialProducts.value = productRepository.getProductById(608).body()
            }
            catch(e:Exception){
            }
        }
        return specialProducts
    }
}