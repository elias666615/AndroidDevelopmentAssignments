package com.example.cam_map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.cam_map.databinding.ActivityMapBinding
import com.google.android.gms.location.*
import java.util.*

class map : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    val mCurrentLocation = MutableLiveData<LocationModel>()
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val SECOND = 1000L
    var locationCallBackRequest = LocationRequest.create().apply {
        interval = 10 * SECOND
        fastestInterval = 5*SECOND
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val logTag = map::class.java.simpleName
    private lateinit var locationCallBack: LocationCallback
    fun startLocationTracking(context: Context) {
        println("*********** Called ************")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallBack = object: LocationCallback() {
            override fun onLocationResult(locations: LocationResult) {
                super.onLocationResult(locations)
                for(location in locations.locations) {
                    mCurrentLocation.postValue(LocationModel(location, Calendar.getInstance().timeInMillis))
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionHelper.askForLocationPermission(this)
            return
        }
        mFusedLocationClient.requestLocationUpdates(locationCallBackRequest, locationCallBack, null)
    }

    fun stopLocationTracking() {
        mFusedLocationClient.removeLocationUpdates(locationCallBack)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        stopLocationTracking()
    }

    override fun onResume() {
        super.onResume()
        checkPermissionsAndSetView()
    }

    private fun checkPermissionsAndSetView() {
        if(PermissionHelper.checkPermissionsGranted(this)) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
            startLocationTracking(this)
        }
        else {
            PermissionHelper.askForLocationPermission(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionHelper.REQUEST_CODE_FOR_PERMISSION) {
            checkPermissionsAndSetView()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mCurrentLocation.observe(this, androidx.lifecycle.Observer {
            Log.e(logTag, "${it.location.latitude}, ${it.location.longitude}, ${it.timeStamp}")
            val location = LatLng(it.location.latitude, it.location.longitude)
            mMap.addMarker(MarkerOptions().position(location).title("${it.location.latitude}, ${it.location.longitude}"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        })
        mMap.isMyLocationEnabled=true
        mMap.uiSettings.isZoomControlsEnabled=true
        mMap.uiSettings.isCompassEnabled=true
        mMap.setMinZoomPreference(6f)
        mMap.setMaxZoomPreference(14f)
    }
}