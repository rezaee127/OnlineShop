package com.example.onlineshop.ui.detail


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentDetailBinding
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.model.ReviewsItem
import com.example.onlineshop.ui.adapters.ImageAdapter
import com.example.onlineshop.ui.adapters.ProductsItemAdapter
import com.example.onlineshop.ui.adapters.ReviewAdapter
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    var pagerSnapHelper = PagerSnapHelper()
    var listOfProducts=ArrayList<ProductsItem>()
    var productMap=HashMap<Int,Int>()
    lateinit var product:ProductsItem
    var customer: CustomerItem?=null
    var productRating=0
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
        setRatingSpinner()
        checkConnectivity()
        retry(id)
        initView(id)
    }



    private fun initView(id:Int) {
        vModel.getProductById(id)
        vModel.getReviews(id)
        submitComment(id)

        vModel.product.observe(viewLifecycleOwner){
            setView(it)
            product=it
            setRelatedProduct(product)
        }

        binding.btnAddToCart.setOnClickListener {
            goToCartFragment(product)
        }
        

        setReviews()

    }

    private fun submitComment(productId:Int) {
        customer=vModel.getCustomerFromShared()
        binding.btnSubmitComment.setOnClickListener{
            if (customer==null){
                Toast.makeText(requireContext(),"برای ثبت نظر ابتدا باید ثبت نام کنید", Toast.LENGTH_SHORT).show()

            } else{
                binding.clLoadingInDetail.visibility=View.GONE
                binding.svDetail.visibility=View.GONE
                binding.clDetail.visibility=View.GONE
                binding.svSubmitComment.visibility=View.VISIBLE
                saveReview(productId)
            }
        }
    }

    private fun saveReview(productId: Int) {
        binding.btnCreateReview.setOnClickListener {
            if (binding.tfReview.editText?.text.isNullOrBlank())
                binding.tfReview.error = "لطفا نظزتان را وارد کنید"
            else{
                vModel.createReview(ReviewsItem(productId,binding.tfReview.editText?.text.toString(),
                    customer?.firstName+customer?.lastName,customer!!.email,productRating))
            }
        }

    }


    private fun setRatingSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.rating_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.rateSpinner.adapter = adapter
        }

        binding.rateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val spinnerItem = p0?.getItemAtPosition(p2).toString()
                productRating= when (spinnerItem) {
                    "1" -> 1
                    "2" -> 2
                    "3" -> 3
                    "4" -> 4
                    else -> 5
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }


    @SuppressLint("SetTextI18n")
    private fun setView(product:ProductsItem) {
        val galleryAdapter=ImageAdapter()
        binding.rvGallery.adapter=galleryAdapter
        galleryAdapter.submitList(product.images)

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


    fun goToCartFragment(product: ProductsItem){
        listOfProducts=vModel.getArrayFromShared()
        productMap= vModel.getHashMapFromShared()
        if (productMap.contains(product.id)){
            Toast.makeText(requireContext(),"این کالا در سبد خرید موجود است", Toast.LENGTH_SHORT).show()
        }else{
            productMap[product.id]=1
            vModel.saveHashMapInShared(productMap)
            listOfProducts.add(product)
            vModel.saveArrayInShared(listOfProducts)
            Toast.makeText(requireContext(),"این کالا به سبد خرید اضافه شد", Toast.LENGTH_SHORT).show()
            //findNavController().navigate(R.id.action_detailFragment_to_cartFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setReviews() {
        val reviewAdapter=ReviewAdapter()
        binding.rvReviews.adapter=reviewAdapter
        vModel.reviewsList.observe(viewLifecycleOwner){
            reviewAdapter.submitList(it)
            if (it.isNullOrEmpty())
                binding.tvReview.text="نظرات کاربران : \n\n\n\n\t\t\t\tنظری برای این محصول ثبت نشده است"
//            حذف نظرات تایید نشده
//            var verifiedList=ArrayList<ReviewsItem>()
//            for (review in it){
//                if (review.verified)
//                    verifiedList.add(review)
//            }
//            reviewAdapter.submitList(verifiedList)

        }
    }

    private fun setRelatedProduct(product: ProductsItem) {
        val relatedProductAdapter=ProductsItemAdapter{id->showDetailOfRelatedProduct(id)}
        binding.rvRelatedProduct.adapter=relatedProductAdapter

        var stringOfIdsRelatedProducts=""
        for (id in product.relatedIds) {
            stringOfIdsRelatedProducts += "$id,"
        }

        vModel.getRelatedProducts(stringOfIdsRelatedProducts)

        vModel.relatedProducts.observe(viewLifecycleOwner){
            relatedProductAdapter.submitList(it)
        }
    }

    private fun showDetailOfRelatedProduct(id: Int) {
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_detailFragment_self,bundle)
    }


    private fun retry(id: Int) {
        binding.btnRefresh.setOnClickListener {
            vModel.getProductById(id)
            vModel.getReviews(id)
        }
    }

    private fun checkConnectivity() {
        vModel.detailStatus.observe(viewLifecycleOwner){
            when (it) {
                ApiStatus.LOADING -> {
                    binding.clLoadingInDetail.visibility=View.VISIBLE
                    binding.clErrorInDetail.visibility=View.GONE
                    binding.svDetail.visibility=View.GONE
                    binding.clDetail.visibility=View.GONE
                }
                ApiStatus.ERROR -> {
                    Toast.makeText(requireContext(),"خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                    binding.btnError.text=vModel.errorMessage
                    binding.clErrorInDetail.visibility=View.VISIBLE
                    binding.clLoadingInDetail.visibility=View.GONE
                    binding.svDetail.visibility=View.GONE
                    binding.clDetail.visibility=View.GONE
                }
                else -> {
                    binding.clErrorInDetail.visibility=View.GONE
                    binding.clLoadingInDetail.visibility=View.GONE
                    binding.svDetail.visibility=View.VISIBLE
                    binding.clDetail.visibility=View.VISIBLE
                }
            }
        }

        vModel.reviewStatus.observe(viewLifecycleOwner){
            when (it) {
                ApiStatus.LOADING -> binding.clLoadingInDetail.visibility=View.VISIBLE
                ApiStatus.ERROR -> {
                    binding.clLoadingInDetail.visibility=View.GONE
                    Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(requireContext(),"دیدگاه شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show()
                    binding.clLoadingInDetail.visibility=View.GONE
                }
            }
        }
    }

}