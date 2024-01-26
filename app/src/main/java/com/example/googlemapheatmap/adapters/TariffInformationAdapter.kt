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
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        binding=InformationTariffViewBinding.inflate(LayoutInflater.from(context))
        val markerInfo = marker.tag
        if (markerInfo != null) {
            binding!!.tariffTextview.text = "start tariff for this place is $markerInfo"
        } else {
            binding!!.tariffTextview.text = "start tariff for this place is not avaliable"

        }

        return binding!!.root
    }
}


