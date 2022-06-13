package com.example.onlineshop.ui.detail


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
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentDetailBinding
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.adapters.ImageAdapter
import com.example.onlineshop.ui.home.ApiStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    var pagerSnapHelper = PagerSnapHelper()
    val vModel:DetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title="جزئیات کالا"
        if(savedInstanceState == null){
            pagerSnapHelper = PagerSnapHelper()
        }
        val id=requireArguments().getInt("id")

        checkConnectivity()
        retry(id)
        initView(id)
    }

    private fun retry(id: Int) {
        binding.btnRefresh.setOnClickListener {
            vModel.getProductById(id)
        }
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            if(it == ApiStatus.ERROR){
                Toast.makeText(requireContext(),"خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                binding.clErrorInDetail.visibility=View.VISIBLE
                binding.svDetail.visibility=View.GONE
                binding.clDetail.visibility=View.GONE
            }else{
                binding.clErrorInDetail.visibility=View.GONE
                binding.svDetail.visibility=View.VISIBLE
                binding.clDetail.visibility=View.VISIBLE
            }
        }
    }

    private fun initView(id:Int) {
        vModel.getProductById(id)

        vModel.product.observe(viewLifecycleOwner){
            setView(it)
        }

        binding.btnAddToCart.setOnClickListener {
            goToCartFragment(id)
        }

    }



    @SuppressLint("SetTextI18n")
    private fun setView(product:ProductsItem) {
        val adapter=ImageAdapter()
        binding.rvGallery.adapter=adapter
        adapter.submitList(product.images)

        pagerSnapHelper.attachToRecyclerView(binding.rvGallery)
        binding.indicator.attachToRecyclerView(binding.rvGallery, pagerSnapHelper)


        binding.tvTitle.text=product.name
        binding.btnAverageRating.text=product.averageRating
        binding.btnRatingCount.text=product.ratingCount.toString()
        binding.btnPrice.text="${product.price}تومان"

        //حذف کارکترهای اضافی توضیحات محصول
        var str=product.description
        str=str.replace("br","")
        str=str.replace("p","")
        str=str.replace("< />","")
        str=str.replace("</>","")
        str=str.replace("<>","")
        binding.tvDescription.text=str

    }


    fun goToCartFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_detailFragment_to_cartFragment,bundle)
    }
}