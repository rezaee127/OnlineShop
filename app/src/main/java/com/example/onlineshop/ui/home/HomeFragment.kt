package com.example.onlineshop.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
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
        vModel.getProductsOrderByDate()
        vModel.getProductsOrderByRating()
        vModel.getProductsOrderByPopularity()
        initAdapters()
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            if(it ==ApiStatus.ERROR){
                Toast.makeText(requireContext(),"خطا در برقراری ارتباط\n لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show()
                binding.ivError.visibility=View.VISIBLE
                binding.svHomeFragment.visibility=View.GONE
            }else{
                binding.svHomeFragment.visibility=View.VISIBLE
            }
        }
    }

    private fun initAdapters() {
        val adapterForTheProductsOrderByDate=ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvLastProduct.adapter=adapterForTheProductsOrderByDate

        vModel.listOfProductsOrderByDate.observe(viewLifecycleOwner){
            adapterForTheProductsOrderByDate.submitList(it)

        }

        val adapterForTheProductsOrderByPopularity=ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvMostPopular.adapter=adapterForTheProductsOrderByPopularity

        vModel.listOfProductsOrderByPopularity.observe(viewLifecycleOwner){
            adapterForTheProductsOrderByPopularity.submitList(it)
        }

        val adapterForTheProductsOrderByRating=ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvMaxRate.adapter=adapterForTheProductsOrderByRating

        vModel.listOfProductsOrderByRating.observe(viewLifecycleOwner){
            adapterForTheProductsOrderByRating.submitList(it)
        }


    }

    fun goToDetailFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment,bundle)
    }

}
