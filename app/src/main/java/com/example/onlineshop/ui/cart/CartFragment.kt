package com.example.onlineshop.ui.cart

import android.annotation.SuppressLint
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
import com.example.onlineshop.databinding.FragmentCartBinding
import com.example.onlineshop.model.Coupon
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    private val vModel:CartViewModel by viewModels()
    private var listOfProducts=ArrayList<ProductsItem>()
    private var listOfCoupons=listOf<Coupon>()
    private var productMap= HashMap<Int,Int>()
    private var sumPrice=0L
    private var couponCode=""
    private lateinit var productAdapter : CartAdapter
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
        productMap= vModel.getCartHashMapFromShared()
        listOfProducts=vModel.getArrayFromShared()
        setAdapter()
        getPrice()
        productOrder()

        getCoupon()

    }

    private fun getCoupon() {
        binding.btnCoupon.setOnClickListener {
            if (binding.etCoupon.text.isNullOrBlank())
                binding.etCoupon.error = "لطفا کد تخفیف را وارد کنید"
            else {
                if ("code10"==binding.etCoupon.text.toString()) {
                    couponCode = "code10"
                    sumPrice= ((sumPrice * 10.00)/100).toLong()
                }
//                vModel.getCoupons()
//                setCoupon()
            }
        }
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.LOADING -> {
                    binding.clLoadingInCart.visibility = View.VISIBLE
                }
                ApiStatus.ERROR -> {
                    binding.clLoadingInCart.visibility = View.GONE
                    Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.clLoadingInCart.visibility = View.GONE
                   // setCoupon()
                }
            }
        }
    }

    private fun setCoupon() {
        vModel.listOfCoupons.observe(viewLifecycleOwner){
            listOfCoupons=it
            var flag=false
            for (coupon in listOfCoupons){
                if (coupon.code==binding.etCoupon.text.toString()){
                    countDiscount(coupon)
                    flag=true
                    break
                }
            }
                if (!flag)
                    Toast.makeText(requireContext(),"کد تخفیف نامعتبر است", Toast.LENGTH_SHORT).show()

        }

    }

    private fun countDiscount(coupon: Coupon) {
        couponCode=coupon.code
        if(coupon.discountType=="percent"){
            sumPrice= ((sumPrice * coupon.amount.toDouble())/100).toLong()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getPrice() {
        sumPrice=0L
        if (!listOfProducts.isNullOrEmpty()){
            for (product in listOfProducts){
                sumPrice += product.price.toLong() * productMap[product.id]!!
            }
            binding.btnSumPrice.text=sumPrice.toString()+"تومان"
        }else {
            binding.svCart.visibility=View.GONE
            binding.clCartBottom.visibility=View.GONE
            Toast.makeText(requireContext(),"سبد خرید خالی است", Toast.LENGTH_SHORT).show()
        }

    }


    private fun setAdapter() {
        productAdapter= CartAdapter(productMap,
            {detailId->goToDetailFragment(detailId)},
            {product->removeProductFromCart(product)},
            {operator,count,product->changeProductCount(operator,count,product)})
        binding.rvCart.adapter=productAdapter
        productAdapter.submitList(listOfProducts)
    }

    @SuppressLint("SetTextI18n")
    private fun changeProductCount(operator:String, count:Int, product: ProductsItem) {
        if(product.price!=""){
            sumPrice =when(operator){
                "+"-> sumPrice+(product.price.toLong())
                else-> sumPrice-(product.price.toLong())
            }
        }
        binding.btnSumPrice.text= "$sumPrice تومان"
        productMap[product.id]= count
        vModel.saveCartHashMapInShared(productMap)
    }

    internal fun removeProductFromCart(product:ProductsItem){
        listOfProducts.remove(product)
        productAdapter.submitList(listOfProducts)
        setAdapter()
        productMap.remove(product.id)
        getPrice()
        vModel.saveArrayInShared(listOfProducts)
        vModel.saveCartHashMapInShared(productMap)
    }

    private fun goToDetailFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_cartFragment_to_detailFragment,bundle)
    }


    private fun productOrder() {
        val bundle= bundleOf("coupon" to couponCode)
        binding.btnOrder.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_profileFragment,bundle)
        }
    }


}