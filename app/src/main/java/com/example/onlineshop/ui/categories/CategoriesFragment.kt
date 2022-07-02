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
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentCategoriesBinding
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
        //if(savedInstanceState != null){}
        initViews()
    }

    private fun initViews() {
        requireActivity().title="دسته بندی ها"
        checkConnectivity()
        refresh()
        initAdapter()
    }

    private fun refresh() {
        binding.btnRefresh.setOnClickListener {
            vModel.getCategories()
        }
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            when (it) {
                ApiStatus.LOADING -> {
                    binding.pbLoading.visibility=View.VISIBLE
                    binding.btnError.visibility=View.GONE
                    binding.btnRefresh.visibility=View.GONE
                    binding.rvCategories.visibility=View.GONE
                }
                ApiStatus.ERROR -> {
                    Toast.makeText(requireContext(),"خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                    binding.btnError.text=vModel.errorMessage
                    binding.btnError.visibility=View.VISIBLE
                    binding.btnRefresh.visibility=View.VISIBLE
                    binding.rvCategories.visibility=View.GONE
                    binding.pbLoading.visibility=View.GONE
                }
                else -> {
                    binding.btnError.visibility=View.GONE
                    binding.btnRefresh.visibility=View.GONE
                    binding.pbLoading.visibility=View.GONE
                    binding.rvCategories.visibility=View.VISIBLE

                }
            }
        }
    }

    private fun initAdapter() {
        val adapter= CategoriesItemAdapter{ id, name -> goToCategoryProductListFragment(id,name) }
        binding.rvCategories.adapter=adapter
        vModel.listOfCategories.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }


    fun goToCategoryProductListFragment(id:Int,name:String){
        val bundle= bundleOf("id" to id,"name" to name)
        findNavController().navigate(R.id.action_categoriesFragment_to_categoryProductListFragment,bundle)
    }
}