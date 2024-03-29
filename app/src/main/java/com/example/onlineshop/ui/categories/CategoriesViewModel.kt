package com.example.onlineshop.ui.categories


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.CategoriesItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    var status=MutableLiveData<ApiStatus>()
    var listOfCategories=MutableLiveData<List<CategoriesItem>>()

    init {
        getCategories()
    }

    fun getCategories(): LiveData<List<CategoriesItem>>{
        viewModelScope.launch {
            status.value=ApiStatus.LOADING
            try {
                listOfCategories.value=repository.getCategories()
                status.value = ApiStatus.DONE
            }
            catch(e:Exception){
                status.value = ApiStatus.ERROR
            }
        }
        return listOfCategories
    }

}