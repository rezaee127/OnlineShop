package com.example.onlineshop.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentSearchBinding
import com.example.onlineshop.ui.adapters.CategoryProductListAdapter
import com.example.onlineshop.ui.adapters.SearchFilterAdapter
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    val vModel: SearchViewModel by viewModels()
    var category = ""
    var attribute=""
    var attributeTerm=""
    var colorAttributeTerm=""
    var sizeAttributeTerm=""
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
        requireActivity().title = "جستجو"
        checkConnectivity()
        checkConnectivityForGetSearchResult()
        setCategorySpinner()
        setColorAdapter()
        setSizeAdapter()
        setSearchResultAdapter()
        binding.btnSearch.setOnClickListener {
            search()
        }
        buttonReturnClicked()
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

    private fun setColorAdapter() {
        val colorAdapter=SearchFilterAdapter {isCheck, id ->
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
        val sizeAdapter=SearchFilterAdapter{isCheck,id ->
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



    private fun setCategorySpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        }

        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val spinnerItem = p0?.getItemAtPosition(p2).toString()
                category = when (spinnerItem) {
                    "پوشاک زنانه" -> "63"
                    "پوشاک مردانه" -> "64"
                    "مد و پوشاک" -> "62"
                    "کفش" -> "70"
                    "کیف و کوله" -> "124"
                    "فروش ویژه" -> "119"
                    "دیجیتال" -> "52"
                    "گوشی موبایل" -> "53"
                    "ساعت و مچ بند هوشمند" -> "102"
                    "کتاب و هنر" -> "76"
                    "کتاب و مجلات" -> "79"
                    "فیلم" -> "77"
                    "سوپرمارکت" -> "81"
                    "مواد پروتئینی" -> "82"
                    "لبنیات" -> "86"
                    "نوشیدنی ها" -> "95"
                    "لوازم منزل" -> "129"
                    "بهداشت" -> "121"
                    else -> ""
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
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
        val searchResultAadapter = CategoryProductListAdapter { id -> goToDetailFragment(id) }
        binding.rvSearch.adapter = searchResultAadapter
        vModel.listOfSearchedProduct.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                searchResultAadapter.submitList(it)
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
        binding.btnReturn.setOnClickListener {
            binding.crSearch.visibility = View.VISIBLE
            binding.btnSearch.visibility = View.VISIBLE
            binding.rvSearch.visibility = View.GONE
            binding.btnReturn.visibility = View.GONE
        }
    }

    private fun goToDetailFragment(id: Int) {
        val bundle = bundleOf("id" to id)
        findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
    }

}