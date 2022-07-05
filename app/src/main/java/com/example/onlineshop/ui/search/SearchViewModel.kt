package com.example.onlineshop.ui.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.data.errorHandling
import com.example.onlineshop.model.AttributeTerm
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SearchViewModel  @Inject constructor(private val repository: Repository): ViewModel() {
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
                listOfColors.value=repository.getColorList()
                status.value=ApiStatus.DONE
            }
            catch(e:Exception){
                errorMessage=errorHandling(e)
                status.value = ApiStatus.ERROR
            }
        }
        return listOfColors
    }


    fun getSizeList(): LiveData<List<AttributeTerm>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfSizes.value=repository.getSizeList()
                status.value=ApiStatus.DONE
            }
            catch(e:Exception){
                errorMessage= errorHandling(e)
                status.value = ApiStatus.ERROR
            }
        }
        return listOfSizes
    }


    fun searchProducts(searchKey:String,orderBy: String,order: String, category: String,
                       attribute: String,attributeTerm: String):LiveData<List<ProductsItem>>{

        viewModelScope.async {
            searchStatus.value=ApiStatus.LOADING
            try {
                listOfSearchedProduct.value=repository.searchProducts(searchKey,orderBy,order,
                    category,attribute,attributeTerm)

                searchStatus.value=ApiStatus.DONE
            }
            catch(e:Exception){
                errorMessage=errorHandling(e)
                searchStatus.value = ApiStatus.ERROR
            }
        }
        return listOfSearchedProduct
    }
}