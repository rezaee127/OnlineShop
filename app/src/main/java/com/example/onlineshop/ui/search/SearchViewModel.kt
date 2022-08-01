package com.example.onlineshop.ui.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.network.utils.setErrorMessage
import com.example.onlineshop.data.repositories.ProductRepository
import com.example.onlineshop.model.AttributeTerm
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SearchViewModel  @Inject constructor(private val productRepository: ProductRepository): ViewModel() {
    var status= MutableLiveData(ApiStatus.LOADING)
    var searchStatus= MutableLiveData(ApiStatus.LOADING)
    var listOfSearchedProduct=MutableLiveData<List<ProductsItem>>()
    var listOfColors=MutableLiveData<List<AttributeTerm>>()
    var listOfSizes=MutableLiveData<List<AttributeTerm>>()
    var errorMessage=""

//    init {
//        getColorList()
//        getSizeList()
//    }

    fun getColorList(): LiveData<List<AttributeTerm>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                val response=productRepository.getColorList()
                if(response.isSuccessful){
                    listOfColors.value=response.body()
                    status.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    status.value = ApiStatus.ERROR
                }
            }
            catch(e:Exception){
                errorMessage=setErrorMessage(e)
                status.value = ApiStatus.ERROR
            }
        }
        return listOfColors
    }


    fun getSizeList(): LiveData<List<AttributeTerm>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                val response=productRepository.getSizeList()
                if(response.isSuccessful){
                    listOfSizes.value=response.body()
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
        return listOfSizes
    }


    fun searchProducts(searchKey:String,orderBy: String,order: String, category: String,
                       attribute: String,attributeTerm: String):LiveData<List<ProductsItem>>{

        viewModelScope.launch {
            searchStatus.value=ApiStatus.LOADING
            try {
                val response=productRepository.searchProducts(searchKey,orderBy,order,
                                                              category,attribute,attributeTerm)

                if(response.isSuccessful){
                    listOfSearchedProduct.value=response.body()
                    searchStatus.value = ApiStatus.DONE
                }else{
                    errorMessage= setErrorMessage(null,response.code(),response.errorBody())
                    searchStatus.value = ApiStatus.ERROR
                }
            }
            catch(e:Exception){
                errorMessage=setErrorMessage(e)
                searchStatus.value = ApiStatus.ERROR
            }
        }
        return listOfSearchedProduct
    }
}