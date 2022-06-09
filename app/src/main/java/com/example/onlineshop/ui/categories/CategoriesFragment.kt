package com.example.onlineshop.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentCategoriesBinding
import com.example.onlineshop.ui.adapters.CategoriesItemAdapter
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    lateinit var binding:FragmentCategoriesBinding
    val vModel:CategoriesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_categories, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState != null){}

        checkConnectivity()
        initAdapter()
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            if(it == ApiStatus.ERROR){
                binding.ivError.visibility=View.VISIBLE
                Toast.makeText(requireContext(),"خطا در برقراری ارتباط\n لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initAdapter() {
        val adapter=CategoriesItemAdapter{ id -> goToCategoryProductListFragment(id) }
        binding.rvCategories.adapter=adapter
        vModel.listOfCategories.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }


    fun goToCategoryProductListFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_categoriesFragment_to_categoryProductListFragment,bundle)
    }
}