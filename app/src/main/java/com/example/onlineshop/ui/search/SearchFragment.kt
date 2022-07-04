package com.example.onlineshop.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentSearchBinding
import com.example.onlineshop.ui.categories.CategoriesViewModel
import com.example.onlineshop.ui.categoryProductList.CategoryProductListAdapter
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val vModel: SearchViewModel by viewModels()
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private var category = ""
    private var attribute=""
    private var attributeTerm=""
    private var colorAttributeTerm=""
    private var sizeAttributeTerm=""


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
        requireActivity().title = "جستجو"
        vModel.getColorList()
        vModel.getSizeList()
        categoriesViewModel.getCategories()

        checkConnectivity()
        checkConnectivityForGetSearchResult()

        setCategoriesAdapter()
        setColorAdapter()
        setSizeAdapter()
        setSearchResultAdapter()

        binding.btnSearch.setOnClickListener {
            search()
        }
        binding.btnReturn.setOnClickListener {
            buttonReturnClicked()
        }
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.llSearch.visibility = View.GONE
                }
                ApiStatus.ERROR -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.llSearch.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.llSearch.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun setCategoriesAdapter() {
        val categoriesAdapter=SearchCategoriesAdapter{ isCheck, id ->
            if (isCheck){
                category="$category$id,"
            }else{
                category=category.replace("$id,","")
            }
        }
        binding.rvCategories.adapter=categoriesAdapter
        categoriesViewModel.listOfCategories.observe(viewLifecycleOwner){
            categoriesAdapter.submitList(it)
        }
    }

    private fun setColorAdapter() {
        val colorAdapter= SearchFilterAdapter { isCheck, id ->
            if (isCheck){
                colorAttributeTerm= "$colorAttributeTerm$id,"
            }else{
                colorAttributeTerm=colorAttributeTerm.replace("$id,","")
            }

            attributeTerm=colorAttributeTerm
            attribute = "pa_color"
        }
        binding.rvColors.adapter=colorAdapter
        vModel.listOfColors.observe(viewLifecycleOwner){
            colorAdapter.submitList(it)
        }
    }

    private fun setSizeAdapter() {
        val sizeAdapter= SearchFilterAdapter{ isCheck, id ->
            if (isCheck){
                sizeAttributeTerm= "$sizeAttributeTerm$id,"
            }else{
                sizeAttributeTerm=sizeAttributeTerm.replace("$id,","")
            }

            attributeTerm=sizeAttributeTerm
            attribute="pa_size"
        }
        binding.rvSize.adapter=sizeAdapter
        vModel.listOfSizes.observe(viewLifecycleOwner){
            sizeAdapter.submitList(it)
        }
    }



    private fun search() {
        var orderBy = ""
        var order = ""

        when {
            binding.rbBestselling.isChecked -> {
                orderBy = "popularity"
                order = "desc"
            }
            binding.rbCheapest.isChecked -> {
                orderBy = "price"
                order = "asc"
            }
            binding.rbMostExpensive.isChecked -> {
                orderBy = "price"
                order = "desc"
            }
            else -> {
                orderBy = "date"
                order = "desc"
            }
        }


        if (binding.outlinedTextField.editText?.text.isNullOrBlank())
            binding.outlinedTextField.editText?.error = "یک کلمه وارد کنید"
        else {
            if (attributeTerm==""){
                if (attribute=="pa_size"){
                    attribute="pa_color"
                    attributeTerm=colorAttributeTerm
                }else{
                    attribute="pa_size"
                    attributeTerm=sizeAttributeTerm
                }
            }

            vModel.searchProducts(binding.outlinedTextField.editText?.text.toString(),
                orderBy, order,category,attribute,attributeTerm)
        }
    }




    private fun checkConnectivityForGetSearchResult() {
        vModel.searchStatus.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.LOADING ->
                    binding.pbLoading.visibility = View.VISIBLE
                ApiStatus.ERROR -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.pbLoading.visibility = View.GONE
                }
            }
        }
    }


    private fun setSearchResultAdapter() {
        val searchResultAdapter = CategoryProductListAdapter { id -> goToDetailFragment(id) }
        binding.rvSearch.adapter = searchResultAdapter
        vModel.listOfSearchedProduct.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                searchResultAdapter.submitList(it)
                binding.crSearch.visibility = View.GONE
                binding.btnSearch.visibility = View.GONE
                binding.rvSearch.visibility = View.VISIBLE
                binding.btnReturn.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(),"کالایی با این مشخصات یافت نشد", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun buttonReturnClicked() {
        binding.crSearch.visibility = View.VISIBLE
        binding.btnSearch.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
        binding.btnReturn.visibility = View.GONE
        vModel.getColorList()
        vModel.getSizeList()
        categoriesViewModel.getCategories()
    }

    private fun goToDetailFragment(id: Int) {
        val bundle = bundleOf("id" to id)
        findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
    }

}
