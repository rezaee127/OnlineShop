package com.example.onlineshop.ui.cart

import androidx.lifecycle.ViewModel
import com.example.onlineshop.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel  @Inject constructor(private val repository: Repository): ViewModel(){

}