package com.example.onlineshop.ui.categories

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.onlineshop.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repository: Repository, var app: Application): ViewModel() {




}