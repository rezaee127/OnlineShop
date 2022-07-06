package com.example.onlineshop.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.onlineshop.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val lat=intent.getDoubleExtra("lat",0.0)
        val long=intent.getDoubleExtra("long",0.0)

        map.setMinZoomPreference(15.0f)
        map.setMaxZoomPreference(60.0f)
        map.addMarker(
            MarkerOptions()
                .position(LatLng(lat, long))
                .title("Marker in Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
                .zIndex(2.0f),
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat , long)))

    }


}
