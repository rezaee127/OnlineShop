package com.example.onlineshop.ui.search

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
import com.example.onlineshop.databinding.FragmentSearchBinding
import com.example.onlineshop.ui.adapters.CategoryProductListAdapter
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var binding:FragmentSearchBinding
    val vModel:SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        requireActivity().title="جستجو"
        binding.btnSearch.setOnClickListener {
            search()
        }
        buttonReturnClicked()
    }





    private fun search() {
        var orderBy="date"
        var order="desc"
        when {
            binding.rbBestselling.isChecked -> {
                orderBy="popularity"
            }
            binding.rbCheapest.isChecked -> {
                orderBy="price"
                order="asc"
            }
            binding.rbMostExpensive.isChecked -> {
                orderBy="price"
            }
        }
        if (binding.outlinedTextField.editText?.text.isNullOrBlank())
            binding.outlinedTextField.editText?.error="یک کلمه وارد کنید"
        else{
            vModel.searchProducts(binding.outlinedTextField.editText?.text.toString(),orderBy,order)
            checkConnectivity()
        }
    }


    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            when (it) {
                ApiStatus.LOADING ->
                    binding.pbLoading.visibility=View.VISIBLE
                ApiStatus.ERROR -> {
                    binding.pbLoading.visibility=View.GONE
                    Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.pbLoading.visibility=View.GONE
                    setAdapter()
                }
            }
        }
    }


    private fun setAdapter() {
        val adapter= CategoryProductListAdapter {id -> goToDetailFragment(id)}
        binding.rvSearch.adapter=adapter
        vModel.listOfSearchedProduct.observe(viewLifecycleOwner){
            if (it.isNullOrEmpty()){
                Toast.makeText(requireContext(),"کالایی با این مشخصات یافت نشد", Toast.LENGTH_SHORT).show()
            }else{
                adapter.submitList(it)
                binding.crSearch.visibility=View.GONE
                binding.rvSearch.visibility=View.VISIBLE
                binding.btnReturn.visibility=View.VISIBLE
            }
        }
    }


    private fun buttonReturnClicked() {
        binding.btnReturn.setOnClickListener {
            binding.crSearch.visibility=View.VISIBLE
            binding.rvSearch.visibility=View.GONE
            binding.btnReturn.visibility=View.GONE
        }
    }

    fun goToDetailFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_searchFragment_to_detailFragment,bundle)
    }

}