package com.example.onlineshop.ui.detail


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.onlineshop.databinding.FragmentDetailBinding
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.ui.adapters.ImageAdapter
import com.example.onlineshop.ui.home.ApiStatus
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
        checkConnectivity()
        if(savedInstanceState == null){
            pagerSnapHelper = PagerSnapHelper()
        }

        initView()
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            if(it == ApiStatus.ERROR){
                Toast.makeText(requireContext(),"خطا در برقراری ارتباط\n لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show()
                binding.ivError.visibility=View.VISIBLE
                binding.svDetail.visibility=View.GONE
                binding.clDetail.visibility=View.GONE
            }
        }
    }

    private fun initView() {
        val id=requireArguments().getInt("id")

        vModel.getProductById(id)

        vModel.product.observe(viewLifecycleOwner){
            setView(it)
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


}