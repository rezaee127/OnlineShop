package com.example.onlineshop.ui.profile

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentProfileBinding
import com.example.onlineshop.model.*
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mCustomer: CustomerItem
    private val vModel : ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title="پروفایل"
        initViews()

    }

    private fun initViews() {

        checkConnectivity()

        order()

        if (vModel.getCustomerFromShared()!=null){
            enter()
        } else{
            register()
        }


        vModel.customer.observe(viewLifecycleOwner){
            vModel.saveCustomerInShared(it)
            mCustomer=it
            enter()
        }

        vModel.order.observe(viewLifecycleOwner){
            vModel.emptyShoppingCart()
            binding.btnOrder.isEnabled=false
        }

    }

    private fun order() {
        val productIdCountHashMap=vModel.getHashMapFromShared()
        val lineItems=ArrayList<LineItem>()
        if (productIdCountHashMap.isEmpty()){
            binding.btnOrder.isEnabled=false
        }else{
            for (item in productIdCountHashMap){
                val lineItem=LineItem(item.key,item.value)
                lineItems.add(lineItem)
            }
            binding.btnOrder.setOnClickListener {
                vModel.createOrder(OrderItem(0,lineItems,mCustomer.id,
                    Billing(binding.tfFirstName.editText?.text.toString(),
                        binding.tfLastName.editText?.text.toString(),
                        binding.tfEmail.editText?.text.toString(),
                        binding.tfAddress1.editText?.text.toString(),
                        binding.tfAddress2.editText?.text.toString()),
                    Shipping(binding.tfFirstName.editText?.text.toString(),
                        binding.tfLastName.editText?.text.toString(),
                        binding.tfAddress1.editText?.text.toString(),
                        binding.tfAddress2.editText?.text.toString())
                ))
            }
        }
    }

    private fun enter() {
        if (vModel.getHashMapFromShared().isEmpty())
            binding.btnOrder.isEnabled=false
        mCustomer= vModel.getCustomerFromShared()!!
        binding.tfFirstName.editText?.setText(mCustomer.firstName)
        binding.tfLastName.editText?.setText(mCustomer.lastName)
        binding.tfEmail.editText?.setText(mCustomer.email)
        binding.tfAddress1.editText?.setText(mCustomer.billing.address1)
        binding.tfAddress2.editText?.setText(mCustomer.billing.address2)
        binding.tfFirstName.editText?.isEnabled=false
        binding.tfLastName.editText?.isEnabled=false
        binding.tfEmail.editText?.isEnabled=false
        binding.btnRegister.visibility=View.GONE
        binding.btnOrder.visibility=View.VISIBLE
        binding.btnReturnToCart.visibility=View.VISIBLE
    }

    private fun register() {
        val emailRegex =   Regex("\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,6}")
        binding.btnRegister.setOnClickListener {
            when {
                binding.tfFirstName.editText?.text.isNullOrBlank() -> binding.tfFirstName.error = "نام را وارد کنید"
                binding.tfLastName.editText?.text.isNullOrBlank() -> binding.tfLastName.error = "نام خانوادگی را وارد کنید"
                binding.tfAddress1.editText?.text.isNullOrBlank() -> binding.tfAddress1.error = "آدرس را وارد کنید"
                binding.tfEmail.editText?.text.isNullOrBlank() -> binding.tfEmail.error = "ایمیل را وارد کنید"
                !emailRegex.matches(binding.tfEmail.editText?.text.toString()) -> binding.tfEmail.error ="ایمیل اشتباه است"

                else -> {
                    vModel.createCustomer(CustomerItem(0,
                        binding.tfFirstName.editText?.text.toString(),
                        binding.tfLastName.editText?.text.toString(),
                        binding.tfEmail.editText?.text.toString(),
                    Billing(binding.tfFirstName.editText?.text.toString(),
                        binding.tfLastName.editText?.text.toString(),
                        binding.tfEmail.editText?.text.toString(),
                        binding.tfAddress1.editText?.text.toString(),
                        binding.tfAddress2.editText?.text.toString()),
                    Shipping(binding.tfFirstName.editText?.text.toString(),
                        binding.tfLastName.editText?.text.toString(),
                        binding.tfAddress1.editText?.text.toString(),
                        binding.tfAddress2.editText?.text.toString())
                    ))
                }
            }
        }
    }


    private fun checkConnectivity() {
        vModel.customerRequestStatus.observe(viewLifecycleOwner){
            when (it) {
                ApiStatus.LOADING -> binding.pbLoading.visibility=View.VISIBLE
                ApiStatus.ERROR -> {
                    binding.pbLoading.visibility=View.GONE
                    Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                }
                else -> {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setMessage("ثبت نام با موفقیت انجام شد. \nکد کاربری شما : ${mCustomer.id}")
                        .setPositiveButton("متوجه شدم"
                        ) { _, _ -> }.create().show()

                    binding.pbLoading.visibility=View.GONE
                    if (vModel.getHashMapFromShared().isEmpty()){
                        binding.btnOrder.isEnabled=false
                    }
                }
            }

        }

        vModel.orderRequestStatus.observe(viewLifecycleOwner){
            when (it) {
                ApiStatus.LOADING -> binding.pbLoading.visibility=View.VISIBLE
                ApiStatus.ERROR -> {
                    binding.pbLoading.visibility=View.GONE
                    Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                }
                else -> {
                    var orderId=0
                    vModel.order.observe(viewLifecycleOwner){order ->
                        orderId=order.id
                    }
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setMessage("سفارش شما با شماره $orderId ثبت شد")
                        .setPositiveButton("متوجه شدم",
                            DialogInterface.OnClickListener { _, _ ->
                                findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
                            }).create().show()
                    binding.pbLoading.visibility=View.GONE
                    binding.btnOrder.isEnabled=false
                }
            }
        }
    }



}