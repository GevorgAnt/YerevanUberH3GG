package com.example.googlemapheatmap.utills
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.googlemapheatmap.R

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object CustomMarkerUtils {

    fun getCustomMarkerIcon(context: Context,markerText:Int): BitmapDescriptor {
        val customMarkerView = LayoutInflater.from(context).inflate(R.layout.information_tariff_view, null)
        val textView = customMarkerView.findViewById<TextView>(R.id.tariff_textview)
        "start tariff for this place is \n $markerText Դ".also { textView.text = it } //senc android studiona sarqel
        val bitmap = createBitmapFromView(customMarkerView)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun createBitmapFromView(view: View): Bitmap {
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(measuredWidth, measuredHeight)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }
}
