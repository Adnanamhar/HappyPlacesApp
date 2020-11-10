package com.adnanamhar.happyplace.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adnanamhar.happyplace.R
import com.adnanamhar.happyplace.model.HappyPlacesModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mHappyDetailActivity: HappyPlacesModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mHappyDetailActivity = intent.getParcelableExtra(MainActivity.HAPPY_PLACE_DETAILS)

        mHappyDetailActivity.let {
            setSupportActionBar(toolbar_map)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = it?.title

            toolbar_map.setNavigationOnClickListener {
                onBackPressed()
            }

            val supportMapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            supportMapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        val position = LatLng(mHappyDetailActivity!!.latitude, mHappyDetailActivity!!.longitude)

        map!!.addMarker(MarkerOptions().position(position).title(mHappyDetailActivity!!.location))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 40f)
        map.animateCamera(newLatLngZoom)
    }
}