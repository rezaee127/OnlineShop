package com.example.onlineshop.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentProfileBinding
import com.example.onlineshop.model.*
import com.example.onlineshop.ui.home.ApiStatus
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mCustomer: CustomerItem
    private val vModel : ProfileViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var addressList=ArrayList<Address>()

    private lateinit var requestPermissionLauncher : ActivityResultLauncher<Array<String>>
    var isFineLocationPermissionGranted=false
    var isCoarseLocationPermissionGranted=false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissionLauncher =registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
            isFineLocationPermissionGranted=permissions[Manifest.permission.ACCESS_FINE_LOCATION]?:isFineLocationPermissionGranted
            isCoarseLocationPermissionGranted=permissions[Manifest.permission.ACCESS_COARSE_LOCATION]?:isCoarseLocationPermissionGranted
        }
        requireActivity().title="پروفایل"
        initViews()

    }

    @RequiresApi(Build.VERSION_CODES.N)
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

        binding.btnLocation.setOnClickListener {
            getLocation()
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
        binding.btnLocation.visibility=View.VISIBLE
        binding.btnShowOnMap.visibility=View.VISIBLE
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





        @RequiresApi(Build.VERSION_CODES.N)
        fun getLocation(){
            requestPermissions()
           //if( isGooglePlayServicesAvailable(requireContext()))
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //if user already granted any of the permission
            isFineLocationPermissionGranted= ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            isCoarseLocationPermissionGranted= ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED



            val permissionsNeeded= mutableListOf<String>()
            if(!isCoarseLocationPermissionGranted)
                permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if(!isFineLocationPermissionGranted)
                permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)

            if(permissionsNeeded.isNotEmpty()){
                requestPermissionLauncher.launch(permissionsNeeded.toTypedArray())
            }else
                showLocation()

        }
    }





    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w("123", strReturnedAddress.toString())
            } else {
                Log.w("123", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("123", "Canont get Address!")
        }
        return strAdd
    }



    @SuppressLint("MissingPermission")
    private fun showLocation() {
        addressList=vModel.getAddressListFromShared()

        if(!isLocationEnabled(requireContext())){
            Toast.makeText(requireContext(), "لطفا لوکیشن خود را روشن کنید", Toast.LENGTH_SHORT).show()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            location?.let{
                Toast.makeText(requireContext(), "latitude" + it.latitude + " , long=" + it.longitude, Toast.LENGTH_LONG).show()

                binding.tfAddress2.editText?.setText(getCompleteAddressString(it.latitude , it.longitude))

                addressList.add(Address("آدرس ${addressList.size+1}",
                    getCompleteAddressString(it.latitude , it.longitude),it.latitude , it.longitude))

                vModel.saveAddressListInShared(addressList)

                showLocationOnMap(it.latitude , it.longitude)
            }
        }

//        fusedLocationClient.getCurrentLocation(	PRIORITY_BALANCED_POWER_ACCURACY , null).addOnSuccessListener{
//                location : Location? ->
//            location?.let{
//                Toast.makeText(requireContext(), "latitude" + it.latitude + " , long=" + it.longitude, Toast.LENGTH_LONG).show()
//
//                binding.btnShowOnMap.isEnabled=true
//                binding.btnShowOnMap.setOnClickListener {view ->
//                    showLocationOnMap(it.latitude , it.longitude)
//                }
//
//                binding.tfAddress2.editText?.setText(getCompleteAddressString(it.latitude , it.longitude))
//            }
//        }
    }

    private fun showLocationOnMap(lat:Double,long:Double) {
        val intent = Intent(requireContext(), MapActivity::class.java)
        intent.putExtra("lat" , lat)
        intent.putExtra("long" , long)
        startActivity(intent)

    }


    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }
}
