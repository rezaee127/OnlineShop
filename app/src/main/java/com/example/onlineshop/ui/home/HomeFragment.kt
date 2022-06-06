package com.example.onlineshop.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.onlineshop.databinding.FragmentHomeBinding
import com.example.onlineshop.ui.adapters.ProductsItemAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding
    val vModel:HomeFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
    }

    private fun initAdapters() {
        val adapter1=ProductsItemAdapter(){}
        binding.rvLastProduct.adapter=adapter1

        vModel.listOfProductsOrderByDate.observe(viewLifecycleOwner){
            adapter1.submitList(it)

        }

        val adapter2=ProductsItemAdapter(){}
        binding.rvMostPopular.adapter=adapter2

        vModel.listOfProductsOrderByPopularity.observe(viewLifecycleOwner){
            adapter2.submitList(it)
        }

        val adapter3=ProductsItemAdapter(){}
        binding.rvMaxRate.adapter=adapter3

        vModel.listOfProductsOrderByRating.observe(viewLifecycleOwner){
            adapter3.submitList(it)
        }


    }

}