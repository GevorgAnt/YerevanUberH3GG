package com.example.googlemapheatmap

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions


class HexagonDrawer(private val googleMap: GoogleMap) {
    private val hexagons: MutableList<Polygon> = mutableListOf<Polygon>()


    fun drawGradientHexagon(hexagonCoordinates: List<LatLng>, startColor: Int, endColor: Int, startAlpha: Int, endAlpha: Int) {
        val gradientSteps = 2  // Adjust the number of steps as needed

        // Calculate color and alpha steps for gradient color
        val colorSteps = interpolateColors(startColor, endColor, gradientSteps)
        val alphaSteps = interpolateAlphas(startAlpha, endAlpha, gradientSteps)

        for (i in 0 until gradientSteps) {
            val fillColor = colorSteps[i]
            //invisible stroke
            val strokeColor  = Color.argb(0, 255, 0, 0)  // Adjust stroke color as needed
            val alpha = alphaSteps[i]

            val polygonOptions = PolygonOptions()
                .addAll(hexagonCoordinates)
                .strokeColor(strokeColor)
                .strokeWidth(5f)  // Adjust the stroke width as needed
                .fillColor(Color.argb(alpha, Color.red(fillColor), Color.green(fillColor), Color.blue(fillColor)))
                .clickable(true)

            val hexagon = googleMap.addPolygon(polygonOptions)

            //hexagons.add(hexagon)
        }
    }



    private fun interpolateColors(startColor: Int, endColor: Int, steps: Int): List<Int> {
        val colorSteps = mutableListOf<Int>()
        for (i in 0 until steps) {
            val ratio = i.toFloat() / (steps - 1)
            val interpolatedColor = interpolateColor(startColor, endColor, ratio)
            colorSteps.add(interpolatedColor)
        }
        return colorSteps
    }

    private fun interpolateAlphas(startAlpha: Int, endAlpha: Int, steps: Int): List<Int> {
        val alphaSteps = mutableListOf<Int>()
        for (i in 0 until steps) {
            val ratio = i.toFloat() / (steps - 1)
            val interpolatedAlpha = interpolateAlpha(startAlpha, endAlpha, ratio)
            alphaSteps.add(interpolatedAlpha)
        }
        return alphaSteps
    }

    private fun interpolateColor(startColor: Int, endColor: Int, ratio: Float): Int {
        val startA = Color.alpha(startColor)
        val startR = Color.red(startColor)
        val startG = Color.green(startColor)
        val startB = Color.blue(startColor)

        val endA = Color.alpha(endColor)
        val endR = Color.red(endColor)
        val endG = Color.green(endColor)
        val endB = Color.blue(endColor)

        val interpolatedA = (startA * (1 - ratio) + endA * ratio).toInt()
        val interpolatedR = (startR * (1 - ratio) + endR * ratio).toInt()
        val interpolatedG = (startG * (1 - ratio) + endG * ratio).toInt()
        val interpolatedB = (startB * (1 - ratio) + endB * ratio).toInt()

        return Color.argb(interpolatedA, interpolatedR, interpolatedG, interpolatedB)
    }

    private fun interpolateAlpha(startAlpha: Int, endAlpha: Int, ratio: Float): Int {
        return (startAlpha * (1 - ratio) + endAlpha * ratio).toInt()
    }
}