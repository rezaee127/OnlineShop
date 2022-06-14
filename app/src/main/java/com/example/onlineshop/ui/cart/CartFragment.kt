package com.example.onlineshop.ui.cart

import android.content.Context
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
import com.example.onlineshop.databinding.FragmentCartBinding
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.adapters.CartAdapter
import com.example.onlineshop.ui.home.ApiStatus
import com.example.onlineshop.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : Fragment() {
    lateinit var binding:FragmentCartBinding
    val vModel : CartViewModel by viewModels()
    val vmodel2:HomeViewModel by viewModels()
    var arrayOfProductIds=ArrayList<Int>()
    var listOfProducts=ArrayList<ProductsItem>()
    var sumPrice=0L
    lateinit var productAdapter : CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        requireActivity().title="سبد خرید"
        getArrayFromShared()
        addProductsToList()

        retry()
    }


    private fun retry() {
        binding.btnError.setOnClickListener {
            if (!arrayOfProductIds.isNullOrEmpty()) {
                vModel.getProductById(arrayOfProductIds[0])
            }else
                Toast.makeText(requireContext(),"سبد خرید خالی است", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addProductsToList() {

        if (!arrayOfProductIds.isNullOrEmpty()){
            vModel.getProductById(arrayOfProductIds[0])
            checkConnectivity()
            for (id in arrayOfProductIds){
                vModel.getProductById(id)
                vModel.product.observe(viewLifecycleOwner){
                    listOfProducts.add(it)
                }
            }
            setAdapter()
        }else
            Toast.makeText(requireContext(),"سبد خرید خالی است", Toast.LENGTH_SHORT).show()
    }

    private fun setAdapter() {
        //if (!listOfProducts.isNullOrEmpty()){
            productAdapter=CartAdapter(
                {detailId->goToDetailFragment(detailId)},
                {deleteId->removeProductFromCart(deleteId)},
                {count,price->getSumPrice(count,price) })

            binding.rvCart.adapter=productAdapter
            productAdapter.submitList(listOfProducts)

       // }
        vmodel2.getProductsOrderByDate().observe(viewLifecycleOwner){
            productAdapter.submitList(it)
        }
    }

    private fun getSumPrice(count: Int, price: String):Long {
        if(price!="")
        sumPrice += count * (price.toLong())
        return sumPrice
    }


    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            if(it == ApiStatus.ERROR){
                binding.clErrorInCart.visibility=View.VISIBLE
                binding.clCartBottom.visibility=View.GONE
                binding.svCart.visibility=View.GONE
                Toast.makeText(requireContext(),"خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
            }else{
                binding.clErrorInCart.visibility=View.GONE
                binding.clCartBottom.visibility=View.VISIBLE
                binding.svCart.visibility=View.VISIBLE
            }
        }
    }


    private fun getArrayFromShared() {
        val pref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        val size: Int = pref.getInt("array_size", 0)

        if (size != 0 ) {
            for (i in 0 until size)
                arrayOfProductIds.add(pref.getInt("array_$i", 0))
        }

    }

    private fun saveArrayToShared() {
        val pref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        val edit= pref.edit()
        edit.clear()
        edit.putInt("array_size", arrayOfProductIds.size)
        for (j in arrayOfProductIds.indices)
            edit.putInt("array_$j", arrayOfProductIds[j])
        edit.apply()
    }


    private fun removeProductFromCart(id:Int){
        arrayOfProductIds.remove(id)
        saveArrayToShared()

        for (product in listOfProducts){
            if (product.id==id)
                listOfProducts.remove(product)
        }
        productAdapter.submitList(listOfProducts)
    }

    private fun goToDetailFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_cartFragment_to_detailFragment,bundle)
    }


}