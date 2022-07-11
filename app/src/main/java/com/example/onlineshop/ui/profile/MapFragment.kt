package com.example.onlineshop.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding:FragmentMapBinding
    private lateinit var map: GoogleMap
    var latitude=0.0
    var longitude=0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnSaveAddress.setOnClickListener {
            saveLocation(latitude,longitude)
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val lat=requireArguments().getDouble("lat")
        val long=requireArguments().getDouble("long")

        map.setMinZoomPreference(15.0f)
        map.setMaxZoomPreference(60.0f)
        map.addMarker(

            MarkerOptions()
                .position(LatLng(lat, long))
                .title("مکان شما")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_red))
                .draggable(true)
                .zIndex(2.0f)
        )

        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat , long)))

        //var temp: LatLng? = null
        map.setOnMarkerDragListener(object : OnMarkerDragListener {

            override fun onMarkerDragStart(marker: Marker) {
                //temp = marker.position
            }

            override fun onMarkerDragEnd(marker: Marker) {
                //marker.position = temp!!

                latitude =  marker.position.latitude
                longitude  =  marker.position.longitude
            }

            override fun onMarkerDrag(marker: Marker) {
                //temp = marker.position
                //marker.position = temp!!
            }
        })



    }

    private fun saveLocation(lat:Double,long:Double) {
        val bundle= bundleOf("lat" to lat , "long" to long)
        findNavController().navigate(R.id.action_mapFragment_to_profileFragment,bundle)
    }
}
