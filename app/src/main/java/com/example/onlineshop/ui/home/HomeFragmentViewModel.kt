package com.example.onlineshop.ui.home

import androidx.lifecycle.ViewModel
import com.example.onlineshop.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val repository: Repository): ViewModel() {
}