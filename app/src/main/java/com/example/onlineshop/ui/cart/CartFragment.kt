package com.example.onlineshop.ui.cart


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentCartBinding
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.adapters.CartAdapter
import dagger.hilt.android.AndroidEntryPoint


const val KEY_PREF="cart"

@AndroidEntryPoint
class CartFragment : Fragment() {
    lateinit var binding:FragmentCartBinding
    var listOfProducts=ArrayList<ProductsItem>()
    var sumPrice=0L
    val sharedPref=SharedPref()
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
        listOfProducts=sharedPref.getArrayFromShared(requireContext(),KEY_PREF)
        setAdapter()
        getPrice()
    }

    private fun getPrice() {
        val productList=sharedPref.getArrayFromShared(requireContext(),KEY_PREF)

        if (!productList.isNullOrEmpty()){
            for (product in productList){
                sumPrice += product.price.toLong()
            }
        }
        binding.btnSumPrice.text=sumPrice.toString()+"تومان"
    }


    private fun setAdapter() {
        if (!listOfProducts.isNullOrEmpty()){
            productAdapter=CartAdapter(
                {detailId->goToDetailFragment(detailId)},
                {count,product->removeProductFromCart(count,product)},
                {count,price->plusPrice(count,price)},
                {count,price->minusPrice(count,price) })

            binding.rvCart.adapter=productAdapter
            productAdapter.submitList(listOfProducts)

        }else
            Toast.makeText(requireContext(),"سبد خرید خالی است", Toast.LENGTH_SHORT).show()

    }

    private fun plusPrice(count: Int, price: String):Long {

        if(price!="")
        sumPrice +=  (price.toLong())

        binding.btnSumPrice.text=sumPrice.toString()+" تومان"
        return sumPrice
    }
    private fun minusPrice(count: Int, price: String):Long {

        if(price!="")
            sumPrice -=  (price.toLong())

        binding.btnSumPrice.text=sumPrice.toString()+" تومان"
        return sumPrice
    }




    private fun removeProductFromCart(count:Int,product:ProductsItem){
       listOfProducts.remove(product)
        sharedPref.saveArrayToShared(requireContext(),KEY_PREF,listOfProducts)
        sumPrice-= (count * product.price.toLong())
        setAdapter()
    }

    private fun goToDetailFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_cartFragment_to_detailFragment,bundle)
    }



}