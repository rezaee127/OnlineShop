package com.example.onlineshop.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentHomeBinding
import com.example.onlineshop.ui.MainActivity
import com.example.onlineshop.ui.home.slider.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator



@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val vModel:HomeViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var imageList=  ArrayList<String>()
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
        changeTheme()
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
        val adapterForTheProductsOrderByDate= ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvLastProduct.adapter=adapterForTheProductsOrderByDate

        vModel.listOfProductsOrderByDate.observe(viewLifecycleOwner){
            adapterForTheProductsOrderByDate.submitList(it)
        }

        val adapterForTheProductsOrderByPopularity= ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvMostPopular.adapter=adapterForTheProductsOrderByPopularity

        vModel.listOfProductsOrderByPopularity.observe(viewLifecycleOwner){
            adapterForTheProductsOrderByPopularity.submitList(it)
        }

        val adapterForTheProductsOrderByRating= ProductsItemAdapter{ id -> goToDetailFragment(id) }
        binding.rvMaxRate.adapter=adapterForTheProductsOrderByRating

        vModel.listOfProductsOrderByRating.observe(viewLifecycleOwner){
            adapterForTheProductsOrderByRating.submitList(it)
        }


    }

    private fun goToDetailFragment(id:Int){
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment,bundle)
    }







    private fun changeTheme() {
        val pref = requireActivity().getSharedPreferences("setTheme", Context.MODE_PRIVATE)
        if (pref.getString("theme", "").isNullOrEmpty())
            binding.rbTheme0.isChecked=true
        when(pref.getString("theme", "")) {
           "0" -> binding.rbTheme0.isChecked=true
           "1" -> binding.rbTheme1.isChecked=true
           "2" -> binding.rbTheme2.isChecked=true
           "3" -> binding.rbTheme3.isChecked=true
           "4" -> binding.rbTheme4.isChecked=true
        }
        val arrayOfCheckBoxes=arrayOf(binding.rbTheme0,binding.rbTheme1,
            binding.rbTheme2,binding.rbTheme3,binding.rbTheme4)

        for (i in arrayOfCheckBoxes.indices){
            arrayOfCheckBoxes[i].setOnCheckedChangeListener { _, b ->
                if (b){
                    setTheme("$i")
                }
            }
        }
    }


    private fun setTheme(str:String) {
        val pref = requireActivity().getSharedPreferences("setTheme", Context.MODE_PRIVATE)
        pref.edit().putString("theme", str).apply()
        val intent= Intent(activity,MainActivity::class.java)
        requireActivity().finishAffinity()
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.theme_menu  -> {
                showThemeRadioBottoms()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun showThemeRadioBottoms() {

        if (binding.llTheme.isVisible){
            binding.llTheme.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.llTheme.visibility = View.GONE
                    }
                })
        }else {
            binding.llTheme.animate()
                .alpha(1f)
                .setDuration(100)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.llTheme.visibility = View.VISIBLE
                    }
                })

        }


        //binding.llTheme.isVisible=!binding.llTheme.isVisible
    }


}
