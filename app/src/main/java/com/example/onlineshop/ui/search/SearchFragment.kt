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
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    val vModel: SearchViewModel by viewModels()
    var category = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view?.findViewById<Spinner>(R.id.category_spinner)?.onItemSelectedListener = this
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
        //binding.categorySpinner.onItemSelectedListener = this
        initViews()
    }

    private fun initViews() {
        requireActivity().title = "جستجو"
        setCategorySpinner()
        binding.btnSearch.setOnClickListener {
            search()
        }
        buttonReturnClicked()
    }


    private fun search() {
        var orderBy = "date"
        var order = "desc"

        when {
            binding.rbBestselling.isChecked -> {
                orderBy = "popularity"
            }
            binding.rbCheapest.isChecked -> {
                orderBy = "price"
                order = "asc"
            }
            binding.rbMostExpensive.isChecked -> {
                orderBy = "price"
            }
        }


        if (binding.outlinedTextField.editText?.text.isNullOrBlank())
            binding.outlinedTextField.editText?.error = "یک کلمه وارد کنید"
        else {
            vModel.searchProducts(binding.outlinedTextField.editText?.text.toString(),
                orderBy, order,category)
            checkConnectivity()
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



    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.LOADING ->
                    binding.pbLoading.visibility = View.VISIBLE
                ApiStatus.ERROR -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.pbLoading.visibility = View.GONE
                    setAdapter()
                }
            }
        }
    }


    private fun setAdapter() {
        val adapter = CategoryProductListAdapter { id -> goToDetailFragment(id) }
        binding.rvSearch.adapter = adapter
        vModel.listOfSearchedProduct.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                Toast.makeText(requireContext(),"کالایی با این مشخصات یافت نشد", Toast.LENGTH_SHORT).show()
            } else {
                adapter.submitList(it)
                binding.crSearch.visibility = View.GONE
                binding.rvSearch.visibility = View.VISIBLE
                binding.btnReturn.visibility = View.VISIBLE
            }
        }
    }


    private fun buttonReturnClicked() {
        binding.btnReturn.setOnClickListener {
            binding.crSearch.visibility = View.VISIBLE
            binding.rvSearch.visibility = View.GONE
            binding.btnReturn.visibility = View.GONE
        }
    }

    fun goToDetailFragment(id: Int) {
        val bundle = bundleOf("id" to id)
        findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
    }

}