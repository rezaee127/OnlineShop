package com.example.onlineshop.ui.detail


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.model.ReviewsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    var status = MutableLiveData<ApiStatus>()
    var product = MutableLiveData<ProductsItem>()
    var relatedProducts = MutableLiveData<List<ProductsItem>>()
    var reviewsList = MutableLiveData<List<ReviewsItem>>()

    fun getProductById(id: Int): LiveData<ProductsItem> {
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                product.value = repository.getProductById(id)
                status.value = ApiStatus.DONE
            } catch (e: Exception) {
                status.value = ApiStatus.ERROR
            }
        }
        return product
    }

    fun getReviews(productId: Int): LiveData<List<ReviewsItem>> {
        viewModelScope.launch {
            try {
                reviewsList.value = repository.getReviews(productId)
            } catch (e: Exception) {
            }
        }
        return reviewsList
    }

    fun getRelatedProducts(str:String) {
            viewModelScope.async {
                try {
                    relatedProducts.value = repository.getRelatedProducts(str)
                }
                catch (e: Exception) {
                }
            }
    }
}