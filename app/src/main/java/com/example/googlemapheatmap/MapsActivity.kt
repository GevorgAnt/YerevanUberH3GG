package com.example.googlemapheatmap


import android.Manifest
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.googlemapheatmap.adapters.TariffInformationAdapter
import com.example.googlemapheatmap.databinding.ActivityMapsBinding
import com.example.googlemapheatmap.utills.CustomMarkerUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnPolygonClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import kotlin.random.Random


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnPolygonClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var tariffInfoView: TariffInformationAdapter
    private lateinit var binding: ActivityMapsBinding

    private var hexagonDrawer: HexagonDrawer? = null

    private val hex = YerevanH3LatLon()

    private var firtpos = true
    private var previousMarker: Marker? = null
    private var previousMarkerPos: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tariffInfoView = TariffInformationAdapter(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        hexagonDrawer = HexagonDrawer(googleMap)
        addMyLocationButton()
        drawAllHexagons()
        googleMap.setOnPolygonClickListener(this)
    }

    private fun addMyLocationButton() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Location Permission")
        alertDialogBuilder.setMessage("This app needs location permission to function properly. Grant permission now?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            finish()
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                addMyLocationButton()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }
            else -> {
                showPermissionDialog()
            }
        }
    }

    private fun drawAllHexagons() {
        for ((id, list) in hex.corner.withIndex()) {
            drawHexagon(list, id)
        }
    }

    private fun drawHexagon(corner: Corner, id: Int) {
        corner.id = id
        corner.tariff = Random.nextInt(8, 25) * 100
        corner.color = getPolygonColor(tariff = corner.tariff)
        val alpha = 70
        hexagonDrawer?.drawGradientHexagon(corner, alpha)
    }

    private fun getPolygonColor(tariff: Int): Int {
        return if (tariff < 1200) {
            Color.BLUE
        } else if (tariff in 1200..1899) {
            Color.GREEN
        } else
            Color.RED
    }

    private fun calculateCentroid(points: List<LatLng>): LatLng {
        var xSum = 0.0
        var ySum = 0.0
        for (point in points) {
            xSum += point.latitude
            ySum += point.longitude
        }
        val centerX = xSum / points.size
        val centerY = ySum / points.size
        return LatLng(centerX, centerY)
    }

    private fun animateMarkerToPosition(targetPosition: LatLng, currentPosition: LatLng, markerOptions: MarkerOptions) {
       val marker= mMap.addMarker(markerOptions)
        previousMarker?.remove()
        val animator = ObjectAnimator.ofObject(
            marker,
            "position",
            LatLngEvaluator(),
            currentPosition,
            targetPosition,
        )
        animator.duration = 300
        animator.start()
        previousMarker=marker
    }

    override fun onPolygonClick(clickPolygon: Polygon) {
        if (firtpos) {
            previousMarkerPos = calculateCentroid(clickPolygon.points)
            firtpos=false
        }
        var tariff = 0
        for ((polygon, tag) in HexagonDrawer.hexagons) {
            if (clickPolygon == polygon) {
                for (corner in hex.corner) {
                    if (corner.id == tag)
                        tariff = corner.tariff
                }
            }
        }
        val customMarkerIcon = CustomMarkerUtils.getCustomMarkerIcon(this, tariff)
        val markerOptions = MarkerOptions()
            .position(calculateCentroid(clickPolygon.points))
            .alpha(.8F)
            .icon(customMarkerIcon)
        animateMarkerToPosition(calculateCentroid(clickPolygon.points), previousMarkerPos!!, markerOptions)
        previousMarkerPos = markerOptions.position
    }
    private inner class LatLngEvaluator : TypeEvaluator<LatLng> {
        override fun evaluate(fraction: Float, startValue: LatLng, endValue: LatLng): LatLng {
            val lat = (endValue.latitude - startValue.latitude) * fraction + startValue.latitude
            val lng = (endValue.longitude - startValue.longitude) * fraction + startValue.longitude
            return LatLng(lat, lng)
        }
    }
}


