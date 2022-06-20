package com.example.onlineshop.ui.cart


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.onlineshop.data.Repository
import com.example.onlineshop.model.ProductsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel  @Inject constructor(private val repository: Repository,
                                         private val app:Application): AndroidViewModel(app){


    fun saveArrayInShared(list: ArrayList<ProductsItem>?){
        repository.saveArrayInShared(app.applicationContext,list)
    }

    fun getArrayFromShared(): ArrayList<ProductsItem>{
        return repository.getArrayFromShared(app.applicationContext)
    }

    fun saveHashMapInShared(hashMap: HashMap<Int, Int>){
        repository.saveHashMapInShared(app.applicationContext,hashMap)
    }

    fun getHashMapFromShared():HashMap<Int, Int>{
        return repository.getHashMapFromShared(app.applicationContext)
    }

}