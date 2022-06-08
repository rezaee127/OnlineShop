package com.example.onlineshop.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentHomeBinding
import com.example.onlineshop.ui.adapters.ProductsItemAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val vModel:HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        checkConnectivity()
        initAdapters()
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            if(it ==ApiStatus.ERROR){
                binding.ivError.visibility=View.VISIBLE
                binding.svHomeFragment.visibility=View.GONE
            }else{
                binding.svHomeFragment.visibility=View.VISIBLE
            }
        }
    }

    private fun initAdapters() {
        val adapter1=ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvLastProduct.adapter=adapter1

        vModel.listOfProductsOrderByDate.observe(viewLifecycleOwner){
            adapter1.submitList(it)

        }

        val adapter2=ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvMostPopular.adapter=adapter2

        vModel.listOfProductsOrderByPopularity.observe(viewLifecycleOwner){
            adapter2.submitList(it)
        }

        val adapter3=ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvMaxRate.adapter=adapter3

        vModel.listOfProductsOrderByRating.observe(viewLifecycleOwner){
            adapter3.submitList(it)
        }


    }

    fun goToDetailFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment,bundle)
    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.category_menu_item -> {
                findNavController().navigate(R.id.action_homeFragment_to_categoriesFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
