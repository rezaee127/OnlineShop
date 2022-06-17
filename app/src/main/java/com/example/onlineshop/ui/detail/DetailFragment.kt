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
import com.example.onlineshop.ui.adapters.ProductsItemAdapter
import com.example.onlineshop.ui.adapters.ReviewAdapter
import com.example.onlineshop.ui.cart.KEY_PREF
import com.example.onlineshop.ui.cart.getArrayFromShared
import com.example.onlineshop.ui.cart.saveArrayToShared
import com.example.onlineshop.ui.home.ApiStatus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    var pagerSnapHelper = PagerSnapHelper()
    var listOfProducts=ArrayList<ProductsItem>()
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



    private fun initView(id:Int) {
        vModel.getProductById(id)
        vModel.getReviews(id)
        lateinit var product:ProductsItem
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
        listOfProducts=getArrayFromShared(requireContext(),KEY_PREF)
        if (listOfProducts.contains(product)){
            Toast.makeText(requireContext(),"این کالا در سبد خرید موجود است", Toast.LENGTH_SHORT).show()
        }else{
            listOfProducts.add(product)
            saveArrayToShared(requireContext(),KEY_PREF,listOfProducts)
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

}