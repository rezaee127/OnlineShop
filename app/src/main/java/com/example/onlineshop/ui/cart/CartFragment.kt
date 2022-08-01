package com.example.onlineshop.ui.cart

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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
    private var productMap= HashMap<Int,Int>()
    private var sumPrice=0.00
    private var couponCode=""
    private lateinit var productAdapter : CartAdapter

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
        productMap= vModel.getCartHashMapFromShared()
        listOfProducts=vModel.getArrayOfProductFromShared()
        setAdapter()
        getPrice()
        btnShowCouponEditTextClicked()
        getCoupon()
        productOrder()
    }

    private fun btnShowCouponEditTextClicked() {
        binding.ibShowCouponEditText.setOnClickListener {
            showCouponEditText()
        }
    }


    private fun showCouponEditText() {

        if (binding.llCoupon.isVisible){
            binding.llCoupon.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.llCoupon.visibility = View.GONE
                    }
                })
        }else {
            binding.llCoupon.animate()
                .alpha(1f)
                .setDuration(100)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.llCoupon.visibility = View.VISIBLE
                    }
                })

        }

        //binding.llCoupon.isVisible=!binding.llTheme.isVisible
    }



    private fun getCoupon() {
        binding.btnCoupon.setOnClickListener {
            if (binding.etCoupon.text.isNullOrBlank())
                binding.etCoupon.error = "لطفا کد تخفیف را وارد کنید"
            else {
                vModel.getCoupons(binding.etCoupon.text.toString())
            }
            checkConnectivity()
        }

        vModel.listOfCoupons.observe(viewLifecycleOwner){
            when {
                it.isNullOrEmpty() -> {
                    Toast.makeText(requireContext(),"کد تخفیف نامعتبر است", Toast.LENGTH_SHORT).show()
                }
                it.size>1 -> {
                    Toast.makeText(requireContext(),"خطایی رخ داده است\nدر صورت تداوم مشکل با ما تماس بگیرید", Toast.LENGTH_LONG).show()
                }
                else -> {
                    discountCalculation(it[0])
                }
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
                }
            }
        }
    }



    private fun discountCalculation(coupon: Coupon) {

        when {
            sumPrice<coupon.minimumAmount.toDouble() -> {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setMessage("برای استفاده از این تخفیف باید حداقل ${coupon.minimumAmount}تومان خرید کرده باشید")
                    .setPositiveButton("متوجه شدم") { _, _ -> }.create().show()
            }
            coupon.discountType=="percent" -> {
                val discount=((sumPrice * coupon.amount.toDouble())/100)
                if(coupon.maximumAmount.toDouble()!=0.00 && discount>coupon.maximumAmount.toDouble()){
                    sumPrice -=coupon.maximumAmount.toDouble()
                }else{
                    sumPrice -= discount
                }
                setViewAfterDiscount(coupon)

            }
            coupon.discountType=="fixed_cart" -> {
                if (coupon.code=="yalda"){
                    if (sumPrice<coupon.amount.toDouble())
                        sumPrice=0.00
                    else
                        sumPrice-=coupon.amount.toDouble()
                    setViewAfterDiscount(coupon)
                }
                else if(coupon.code=="s9pbkvt9"){
                    if(coupon.maximumAmount.toDouble()!=0.00 && sumPrice>coupon.maximumAmount.toDouble()){
                        sumPrice -=coupon.maximumAmount.toDouble()
                    }else
                        sumPrice =0.00
                    setViewAfterDiscount(coupon)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setViewAfterDiscount(coupon: Coupon){
        binding.btnSumPrice.text="${(String.format("%,.2f", sumPrice)).substringBefore(".")} تومان"
        binding.btnCoupon.isEnabled=false
        binding.etCoupon.isEnabled=false
        binding.etCoupon.setText("")
        couponCode=coupon.code
        productOrder()
    }

    @SuppressLint("SetTextI18n")
    private fun getPrice() {
        sumPrice=0.00
        if (!listOfProducts.isNullOrEmpty()){
            for (product in listOfProducts){
                if(product.price!="") {
                    sumPrice += product.price.toDouble() * productMap[product.id]!!
                }
            }
            binding.btnSumPrice.text= "${(String.format("%,.2f", sumPrice)).substringBefore(".")} تومان"
        }else {
            binding.svCart.visibility=View.GONE
            binding.clCartBottom.visibility=View.GONE
            binding.clEmptyCart.visibility=View.VISIBLE
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
                "+"-> sumPrice+(product.price.toDouble())
                else-> sumPrice-(product.price.toDouble())
            }
        }
        binding.btnSumPrice.text= "${(String.format("%,.2f", sumPrice)).substringBefore(".")} تومان"
        productMap[product.id]= count
        vModel.saveCartHashMapInShared(productMap)
    }

    private fun removeProductFromCart(product:ProductsItem){
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setMessage("آیا میخواهید این کالا را حذف کنید؟")
            .setNegativeButton("خیر") { _, _ -> }
            .setPositiveButton("بله") { _, _ ->
                listOfProducts.remove(product)
                setAdapter()
                productMap.remove(product.id)
                getPrice()
                vModel.saveArrayOfProductInShared(listOfProducts)
                vModel.saveCartHashMapInShared(productMap)
            }.create().show()
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

    override fun onDestroy() {
        super.onDestroy()
        vModel.listOfCoupons.value= listOf(Coupon("0.00","","","0.00","0.00",0))
    }
}