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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentDetailBinding
import com.example.onlineshop.model.CustomerItem
import com.example.onlineshop.model.ProductsItem
import com.example.onlineshop.model.ReviewsItem
import com.example.onlineshop.ui.home.ProductsItemAdapter
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private var pagerSnapHelper = PagerSnapHelper()
    private var listOfProducts=ArrayList<ProductsItem>()
    private var productMap=HashMap<Int,Int>()
    private var product:ProductsItem?=null
    private var customer: CustomerItem?=null
    var productRating=0
    private var reviewMap=HashMap<Int,Int>()
    private val vModel:DetailViewModel by viewModels()


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
        reviewMap=vModel.getReviewHashMapFromShared()
        setReview(id)

        vModel.product.observe(viewLifecycleOwner){
            setView(it)
            product=it
            setRelatedProduct(product)
        }

        binding.btnAddToCart.setOnClickListener {
            addProductToCart(product)
        }

        saveReviewIdInShare(id)

        showReviews()
        binding.ibReturn.setOnClickListener {
            returnFromReviewPage(id)
        }
        deleteReview(id)
    }

    private fun deleteReview(productId:Int) {
        binding.ibDeleteReview.setOnClickListener {
            reviewMap[productId]?.let { it1 -> vModel.deleteReview(it1) }
            vModel.deleteStatus.observe(viewLifecycleOwner){

                when (it) {
                    ApiStatus.LOADING -> binding.clLoadingInDetail.visibility=View.VISIBLE
                    ApiStatus.ERROR -> {
                        binding.clLoadingInDetail.visibility=View.GONE
                        Toast.makeText(requireContext(), vModel.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        vModel.deletedReview.observe(viewLifecycleOwner){ deleteReview ->
                            if(deleteReview.deleted){
                                Toast.makeText(requireContext(),"دیدگاه شما حذف شد", Toast.LENGTH_SHORT).show()
                                reviewMap.remove(productId)
                                vModel.saveReviewHashMapInShared(reviewMap)
                                binding.clLoadingInDetail.visibility=View.GONE
                                returnFromReviewPage(productId)
                            }else{
                                Toast.makeText(requireContext(),"خطایی رخ داده است\n deleted=false", Toast.LENGTH_LONG).show()
                            }
                        }

                    }
                }
            }
        }
    }

    private fun returnFromReviewPage(productId:Int) {
        binding.clLoadingInDetail.visibility=View.GONE
        binding.svDetail.visibility=View.VISIBLE
        binding.clDetail.visibility=View.VISIBLE
        binding.svSubmitComment.visibility=View.GONE
        vModel.getReviews(productId)
    }

    private fun saveReviewIdInShare(productId: Int) {
        vModel.mReview.observe(viewLifecycleOwner){
            reviewMap[productId]=it.id
            vModel.saveReviewHashMapInShared(reviewMap)
        }
    }

    private fun setReview(productId:Int) {
        customer=vModel.getCustomerFromShared()
        binding.btnSubmitComment.setOnClickListener{
            if (customer==null){
                Toast.makeText(requireContext(),"برای ثبت نظر ابتدا باید ثبت نام کنید", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_detailFragment_to_profileFragment)
            } else{
                binding.clLoadingInDetail.visibility=View.GONE
                binding.svDetail.visibility=View.GONE
                binding.clDetail.visibility=View.GONE
                binding.svSubmitComment.visibility=View.VISIBLE

                if(reviewMap[productId]==null){
                    binding.btnEdit.isEnabled=false
                    binding.ibDeleteReview.isEnabled=false
                    binding.btnCreateReview.isEnabled=true
                    binding.tfReview.editText?.setText("")
                    binding.rateSpinner.setSelection(0)
                    registerReview(productId)
                }else{
                    reviewMap=vModel.getReviewHashMapFromShared()
                    vModel.getReviewById(reviewMap[productId]!!,productId)
                    vModel.receivedReview.observe(viewLifecycleOwner){
                        binding.tfReview.editText?.setText(removeExtraCharacters(it.review))
                        binding.rateSpinner.setSelection(5-it.rating)
                        productRating=it.rating
                        binding.btnFavorite.text= it.rating.toString()
                    }
                    binding.btnCreateReview.isEnabled=false

                    editReview(reviewMap[productId]!!)

                }

            }
        }
    }

    private fun editReview(reviewId:Int) {
        binding.btnEdit.setOnClickListener {
            if (binding.tfReview.editText?.text.isNullOrBlank())
                binding.tfReview.error = "لطفا نظزتان را وارد کنید"
            else{
                vModel.editReview(reviewId,binding.tfReview.editText?.text.toString(),productRating)
            }
        }
    }

    private fun registerReview(productId: Int) {
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
        val galleryAdapter= ImageAdapter()
        binding.rvGallery.adapter=galleryAdapter
        galleryAdapter.submitList(product.images)

        pagerSnapHelper.attachToRecyclerView(binding.rvGallery)
        binding.indicator.attachToRecyclerView(binding.rvGallery, pagerSnapHelper)


        binding.tvTitle.text=product.name
        binding.btnAverageRating.text=product.averageRating
        binding.btnRatingCount.text=product.ratingCount.toString()
        binding.btnPrice.text="${product.price}تومان"

        //حذف کارکترهای اضافی توضیحات محصول
        binding.tvDescription.text= removeExtraCharacters(product.description)

    }

    private fun removeExtraCharacters(description:String):String{
        var str=description
        str=str.replace("br","")
        str=str.replace("p","")
        str=str.replace("< />","")
        str=str.replace("</>","")
        str=str.replace("<>","")
        return str
    }


    private fun addProductToCart(product: ProductsItem?){
        listOfProducts=vModel.getArrayFromShared()
        productMap= vModel.getCartHashMapFromShared()
        if (productMap.contains(product?.id)){
            Toast.makeText(requireContext(),"این کالا در سبد خرید موجود است", Toast.LENGTH_SHORT).show()
        }else{
            productMap[product!!.id]=1
            vModel.saveCartHashMapInShared(productMap)
            listOfProducts.add(product)
            vModel.saveArrayInShared(listOfProducts)
            Toast.makeText(requireContext(),"این کالا به سبد خرید اضافه شد", Toast.LENGTH_SHORT).show()
            //findNavController().navigate(R.id.action_detailFragment_to_cartFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showReviews() {
        val reviewAdapter= ReviewAdapter()
        binding.rvReviews.adapter=reviewAdapter
        vModel.reviewsList.observe(viewLifecycleOwner){
            reviewAdapter.submitList(it)
            if (it.isNullOrEmpty())
                binding.tvReviewNotExist.isVisible=true
        }
    }

    private fun setRelatedProduct(product: ProductsItem?) {
        val relatedProductAdapter= ProductsItemAdapter{ id->showDetailOfRelatedProduct(id)}
        binding.rvRelatedProduct.adapter=relatedProductAdapter

        var stringOfIdsRelatedProducts=""
        for (id in product?.relatedIds!!) {
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
            if (product!=null)
                setRelatedProduct(product)
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
                    binding.btnCreateReview.isEnabled=false
                    binding.btnEdit.isEnabled=true
                    binding.ibDeleteReview.isEnabled=true
                }
            }
        }
    }

}