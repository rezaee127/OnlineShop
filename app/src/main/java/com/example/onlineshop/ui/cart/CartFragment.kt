package com.example.onlineshop.ui.cart

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlineshop.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : Fragment() {
    lateinit var binding:FragmentCartBinding
    var arrayOfProductIds=ArrayList<Int>()
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
        val id=requireArguments().getInt("id")
    }



    private fun getArrayFromShared() {
        val pref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        val size: Int = pref.getInt("array_size", 0)

        if (size != 0 ) {
            for (i in 0 until size)
                arrayOfProductIds[i]= pref.getInt("array_$i", 0)
        }

    }

    private fun saveArrayToShared() {
        val pref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        val edit= pref.edit()
        edit.putInt("array_size", arrayOfProductIds.size)
        for (j in arrayOfProductIds.indices)
            edit.putInt("array_$j", arrayOfProductIds[j])
        edit.apply()
    }

}