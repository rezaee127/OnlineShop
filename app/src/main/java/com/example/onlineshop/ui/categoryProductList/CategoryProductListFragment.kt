package com.example.onlineshop.ui.categoryProductList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentCategoryProductListBinding
import com.example.onlineshop.ui.adapters.CategoryProductListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryProductListFragment : Fragment() {
    lateinit var binding: FragmentCategoryProductListBinding
    val vModel:CategoryProductListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryProductListBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_category_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val id=requireArguments().getInt("id")
        vModel.getProductsListInEachCategory(id)

        initAdapter()
    }

    private fun initAdapter() {
        val adapter= CategoryProductListAdapter{ id -> goToDetailFragment(id)}
        binding.rvCategoryListProduct.adapter=adapter
        vModel.listOfProduct.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    fun goToDetailFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_categoryProductListFragment_to_detailFragment,bundle)
    }
}