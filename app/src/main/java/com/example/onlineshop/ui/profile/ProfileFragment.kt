package com.example.onlineshop.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.onlineshop.databinding.FragmentProfileBinding
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var mCustomer: CustomerItem
    val vModel : ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        if (vModel.getCustomerFromShared()!=null){
            enter()
        } else{
            register()
        }


        vModel.customer.observe(viewLifecycleOwner){
            vModel.saveCustomerInShared(it)
            mCustomer=it
            Toast.makeText(requireContext(),"ثبت نام با موفقیت انجام شد", Toast.LENGTH_SHORT).show()
            enter()
        }

    }

    private fun enter() {
        mCustomer= vModel.getCustomerFromShared()!!
        binding.tfFirstName.editText?.setText(mCustomer.firstName)
        binding.tfLastName.editText?.setText(mCustomer.lastName)
        binding.tfEmail.editText?.setText(mCustomer.email)
        binding.tfFirstName.editText?.isEnabled=false
        binding.tfLastName.editText?.isEnabled=false
        binding.tfEmail.editText?.isEnabled=false
        binding.tfPassword.visibility=View.GONE
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
                binding.tfPassword.editText?.text.isNullOrBlank() -> binding.tfPassword.error = "پسورد را وارد کنید"
                binding.tfEmail.editText?.text.isNullOrBlank() -> binding.tfEmail.error = "ایمیل را وارد کنید"
                !emailRegex.matches(binding.tfEmail.editText?.text.toString()) -> binding.tfEmail.error ="ایمیل اشتباه است"

                else -> {
                    vModel.createCustomer(binding.tfFirstName.editText?.text.toString(),
                        binding.tfLastName.editText?.text.toString(),
                        binding.tfPassword.editText?.text.toString(),
                        binding.tfEmail.editText?.text.toString())
                }
            }
        }
    }


    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            when (it) {
                ApiStatus.LOADING -> binding.pbLoading.visibility=View.VISIBLE
                ApiStatus.ERROR -> {
                    binding.pbLoading.visibility=View.GONE
                    Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                }
                else -> binding.pbLoading.visibility=View.GONE
            }

        }
    }



}