package com.example.onlineshop.ui.categoryProductList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentCategoryProductListBinding
import com.example.onlineshop.ui.home.ApiStatus
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
        //if(savedInstanceState != null){}
        val id=requireArguments().getInt("id")
        val name=requireArguments().getString("name")
        requireActivity().title=name
        checkConnectivity()
        refresh(id)
        initView(id)
    }

    private fun refresh(id: Int) {
        binding.btnRefresh.setOnClickListener {
            vModel.getProductsListInEachCategory(id)
        }
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            when (it) {
                ApiStatus.LOADING -> {
                    binding.pbLoading.visibility=View.VISIBLE
                    binding.btnError.visibility=View.GONE
                    binding.btnRefresh.visibility=View.GONE
                    binding.rvCategoryListProduct.visibility=View.GONE
                }
                ApiStatus.ERROR -> {
                    Toast.makeText(requireContext(),"خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                    binding.btnError.text=vModel.errorMessage
                    binding.btnError.visibility=View.VISIBLE
                    binding.btnRefresh.visibility=View.VISIBLE
                    binding.pbLoading.visibility=View.GONE
                    binding.rvCategoryListProduct.visibility=View.GONE
                }
                else -> {
                    binding.btnError.visibility=View.GONE
                    binding.btnRefresh.visibility=View.GONE
                    binding.pbLoading.visibility=View.GONE
                    binding.rvCategoryListProduct.visibility=View.VISIBLE
                }
            }
        }
    }


    private fun initView(id: Int) {
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