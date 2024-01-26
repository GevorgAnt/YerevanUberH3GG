package com.example.googlemapheatmap.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.googlemapheatmap.databinding.InformationTariffViewBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class TariffInformationAdapter(private val context: Context ) : GoogleMap.InfoWindowAdapter {

    private var binding:InformationTariffViewBinding?=null
    override fun getInfoWindow(marker: Marker): View? {
        // Return null to use the default info window
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        // Inflate your custom info window layout
        binding=InformationTariffViewBinding.inflate(LayoutInflater.from(context))

        // Customize the content of the info window
        val markerInfo = marker.tag

        // Customize the content based on your requirements
        if (markerInfo != null) {
            binding!!.tariffTextview.text = "start tariff for this place is $markerInfo"
        } else {
            binding!!.tariffTextview.text = "start tariff for this place is not avaliable"

        }

        return binding!!.root
    }
}


