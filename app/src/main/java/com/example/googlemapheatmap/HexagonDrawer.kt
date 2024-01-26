package com.example.googlemapheatmap

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions


class HexagonDrawer(private val googleMap: GoogleMap) {

    companion object {
        val hexagons: MutableList<Pair<Polygon, Int>> = mutableListOf()

    }
    fun drawGradientHexagon(corner: Corner, alpha: Int) {
        val polygonOptions = PolygonOptions()
            .addAll(corner.coordinates!!)
            .strokeColor(Color.BLACK)
            .strokeWidth(5f)  // Adjust the stroke width as needed
            .fillColor(Color.argb(alpha, Color.red(corner.color), Color.green(corner.color), Color.blue(corner.color)))
            .clickable(true)
        val hexagon = googleMap.addPolygon(polygonOptions)
        hexagons.add(Pair(hexagon, corner.id))
    }

}