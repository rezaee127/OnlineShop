package com.example.onlineshop.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentHomeBinding
import com.example.onlineshop.ui.adapters.ProductsItemAdapter
import com.example.onlineshop.ui.slider.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator



@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val vModel:HomeViewModel by viewModels()
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    var imageList=  ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title="فروشگاه"
        setViewPager(view)
        checkConnectivity()
        refresh()
        initViewModelFunctions()
        initAdapters()
    }

    private fun setViewPager(view: View) {

        viewPager = view.findViewById(R.id.view_pager)
        vModel.specialProducts.observe(viewLifecycleOwner){
            imageList.clear()
            for (image in it.images){
                imageList.add(image.src)
            }
            viewPagerAdapter = ViewPagerAdapter(requireContext(), imageList)
            viewPager.adapter = viewPagerAdapter
            viewPager.rotationY = 180F
            val indicator: CircleIndicator = view.findViewById(R.id.indicator)
            indicator.setViewPager(viewPager)
            //indicator.animatePageSelected(2)
        }
    }

    private fun refresh() {
        binding.btnRefresh.setOnClickListener {
            initViewModelFunctions()
        }
    }

    private fun initViewModelFunctions() {
        vModel.getSpecialProduct()
        vModel.getProductsOrderByDate()
        vModel.getProductsOrderByRating()
        vModel.getProductsOrderByPopularity()
    }

    private fun checkConnectivity() {
        vModel.status.observe(viewLifecycleOwner){
            when (it) {
                ApiStatus.LOADING -> {
                    binding.pbLoading.visibility=View.VISIBLE
                    binding.svHomeFragment.visibility=View.GONE
                    binding.btnError.visibility=View.GONE
                    binding.btnRefresh.visibility=View.GONE
                }
                ApiStatus.ERROR -> {
                    Toast.makeText(requireContext(),"خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                    binding.btnError.text=vModel.errorMessage
                    binding.btnError.visibility=View.VISIBLE
                    binding.btnRefresh.visibility=View.VISIBLE
                    binding.svHomeFragment.visibility=View.GONE
                    binding.pbLoading.visibility=View.GONE
                }
                else -> {
                    binding.svHomeFragment.visibility=View.VISIBLE
                    binding.btnError.visibility=View.GONE
                    binding.btnRefresh.visibility=View.GONE
                    binding.pbLoading.visibility=View.GONE
                }
            }
        }
    }

    private fun initAdapters() {
        val adapterForTheProductsOrderByDate=ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvLastProduct.adapter=adapterForTheProductsOrderByDate

        vModel.listOfProductsOrderByDate.observe(viewLifecycleOwner){
            adapterForTheProductsOrderByDate.submitList(it)

        }

        val adapterForTheProductsOrderByPopularity=ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvMostPopular.adapter=adapterForTheProductsOrderByPopularity

        vModel.listOfProductsOrderByPopularity.observe(viewLifecycleOwner){
            adapterForTheProductsOrderByPopularity.submitList(it)
        }

        val adapterForTheProductsOrderByRating=ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvMaxRate.adapter=adapterForTheProductsOrderByRating

        vModel.listOfProductsOrderByRating.observe(viewLifecycleOwner){
            adapterForTheProductsOrderByRating.submitList(it)
        }


    }

    fun goToDetailFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment,bundle)
    }

}
